package com.muky.toto.controllers;

import com.muky.toto.model.Answer;
import com.muky.toto.service.EuropeCalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculationController implements CalculationApi {

    private final EuropeCalculationService europeCalculationService;

    public CalculationController(EuropeCalculationService europeCalculationService) {
        this.europeCalculationService = europeCalculationService;
    }

    @Override
    public ResponseEntity<String> calculateOdds(String homeTeam, String awayTeam, String language) {
        try {
            if (language == null) {
                language = "hebrew";
            }
            Answer answer = europeCalculationService.calculateAnswer(homeTeam, awayTeam, language);
            return ResponseEntity.ok(answer.answer());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
