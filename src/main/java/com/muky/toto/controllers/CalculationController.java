package com.muky.toto.controllers;

import com.muky.toto.ai_response.TodoPredictionPromptResponse;
import com.muky.toto.model.Answer;
import com.muky.toto.model.LeagueEnum;
import com.muky.toto.model.SupportedLanguageEnum;
import com.muky.toto.service.EuropeCalculationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CalculationController implements CalculationApi {

    private final EuropeCalculationService europeCalculationService;

    public CalculationController(EuropeCalculationService europeCalculationService) {
        this.europeCalculationService = europeCalculationService;
    }

    @Override
    public ResponseEntity<String> calculateOdds(String homeTeam, String awayTeam, String language, LeagueEnum league) {
        log.info("calculateOdds: homeTeam={}, awayTeam={}, language={}, league={}", homeTeam, awayTeam, language, league);
        try {
            if (language == null) {
                language = "english";
            }
            Answer answer = europeCalculationService.calculateAnswer(homeTeam, awayTeam, language, league);
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
            TodoPredictionPromptResponse todoPredictionPromptResponse = europeCalculationService.calculateTotoPrediction(homeTeam, awayTeam, language.getLanguage(), league);
            return ResponseEntity.ok(todoPredictionPromptResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
