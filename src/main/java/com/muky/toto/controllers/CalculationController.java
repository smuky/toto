package com.muky.toto.controllers;

import com.muky.toto.ai_response.ApiFootballPredictionResponse;
import com.muky.toto.ai_response.TodoPredictionPromptResponse;
import com.muky.toto.model.Answer;
import com.muky.toto.model.LeagueEnum;
import com.muky.toto.model.SupportedLanguageEnum;
import com.muky.toto.service.CalculationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CalculationController implements CalculationApi {

    private final CalculationService calculationService;

    public CalculationController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @Override
    public ResponseEntity<String> calculateOdds(String homeTeam, String awayTeam, String language, LeagueEnum league) {
        log.info("calculateOdds: homeTeam={}, awayTeam={}, language={}, league={}", homeTeam, awayTeam, language, league);
        try {
            if (language == null) {
                language = "english";
            }
            Answer answer = calculationService.calculateAnswer(homeTeam, awayTeam, language, league);
            return ResponseEntity.ok(answer.answer());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<TodoPredictionPromptResponse> calculatePrediction(String homeTeam, String awayTeam, SupportedLanguageEnum language, LeagueEnum league) {
        log.info("calculatePrediction: homeTeam={}, awayTeam={}, language={}, league={}", homeTeam, awayTeam, language, league);
        try {
            if (language == null) {
                language = SupportedLanguageEnum.EN;
            }
            TodoPredictionPromptResponse todoPredictionPromptResponse = calculationService.calculateTotoPredictionFromStanding(homeTeam, awayTeam, language.getLanguage(), league);
            return ResponseEntity.ok(todoPredictionPromptResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<ApiFootballPredictionResponse> getPredictionFromApiFootball(int fixtureId, String language) {
        log.info("getPredictionFromApiFootball: fixtureId={}, language={}", fixtureId, language);
        try {
            ApiFootballPredictionResponse prediction = calculationService.getPredictionFromApiFootball(fixtureId, language);
            return ResponseEntity.ok(prediction);
        } catch (IllegalArgumentException e) {
            log.error("Invalid fixture ID: {}", fixtureId, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error getting prediction for fixture: {}", fixtureId, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
