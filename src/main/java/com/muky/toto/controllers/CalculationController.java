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
    public ResponseEntity<TodoPredictionPromptResponse> calculatePrediction(String predictorId, String homeTeam,
                                                                                     String awayTeam, int fixtureId, LeagueEnum league, String language) {
        TodoPredictionPromptResponse todoPredictionPromptResponse;
        log.info("calculatePrediction: predictorId={}, fixtureId={}, language={}", predictorId, fixtureId, language);
        try {
            if (language == null || language.isEmpty()) {
                language = "en";
            }
            if (fixtureId == 0) {
                 todoPredictionPromptResponse = calculationService.calculateTotoPredictionFromStanding(predictorId, homeTeam,
                         awayTeam, language, league);
            } else {
                todoPredictionPromptResponse = calculationService.getPredictionFromApiFootball(predictorId, homeTeam, awayTeam, fixtureId, language);
            }
            return ResponseEntity.ok(todoPredictionPromptResponse);
        } catch (IllegalArgumentException e) {
            log.error("Invalid fixture ID: {}", fixtureId, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error getting prediction for fixture: {}", fixtureId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
