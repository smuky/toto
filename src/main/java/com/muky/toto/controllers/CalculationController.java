package com.muky.toto.controllers;

import com.muky.toto.ai_response.ApiFootballPredictionResponse;
import com.muky.toto.ai_response.BatchFixturePredictionResponse;
import com.muky.toto.ai_response.TodoPredictionPromptResponse;
import com.muky.toto.model.Answer;
import com.muky.toto.model.LeagueEnum;
import com.muky.toto.model.SupportedLanguageEnum;
import com.muky.toto.service.CalculationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<TodoPredictionPromptResponse> getPredictionFromApiFootball(int fixtureId, String language) {
        log.info("getPredictionFromApiFootball: fixtureId={}, language={}", fixtureId, language);
        try {
            if (language == null || language.isEmpty()) {
                language = "en";
            }
            TodoPredictionPromptResponse predictionFromApiFootball = calculationService.getPredictionFromApiFootball(fixtureId, language);
            return ResponseEntity.ok(predictionFromApiFootball);
        } catch (IllegalArgumentException e) {
            log.error("Invalid fixture ID: {}", fixtureId, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error getting prediction for fixture: {}", fixtureId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<BatchFixturePredictionResponse> getBatchFixturePredictions(List<Integer> fixtureIds, String language) {
        log.info("getBatchFixturePredictions: fixtureIds={}, language={}", fixtureIds, language);
        try {
            if (fixtureIds == null || fixtureIds.isEmpty()) {
                log.error("Fixture IDs list is empty or null");
                return ResponseEntity.badRequest().build();
            }
            
            //BatchFixturePredictionResponse batchPrediction = calculationService.getBatchFixturePredictions(fixtureIds, language);
            return null;
        } catch (IllegalArgumentException e) {
            log.error("Invalid fixture IDs: {}", fixtureIds, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error getting batch predictions for fixtures: {}", fixtureIds, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
