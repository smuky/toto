package com.muky.toto.controllers;

import com.muky.toto.model.Answer;
import com.muky.toto.service.CalculationService;
import com.muky.toto.service.EuropeCalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CalculationController implements CalculationApi {

    private final CalculationService calculationService;
    private final EuropeCalculationService europeCalculationService;

    public CalculationController(CalculationService calculationService, EuropeCalculationService europeCalculationService) {
        this.calculationService = calculationService;
        this.europeCalculationService = europeCalculationService;
    }

    @Override
    public ResponseEntity<String> calculateOdds(String homeTeam, String awayTeam) {
        try {
            Answer answer = calculationService.calculateAnswer(homeTeam, awayTeam);
            return ResponseEntity.ok(answer.answer());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error fetching data: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> calculateEuropeLeagueOdds(String homeTeam, String awayTeam) {
        try {
            Answer answer = europeCalculationService.calculateAnswer(homeTeam, awayTeam);
            return ResponseEntity.ok(answer.answer());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
