package com.muky.toto.controllers;

import com.muky.toto.model.Answer;
import com.muky.toto.service.CalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CalculationController implements CalculationApi {

    private final CalculationService calculationService;

    public CalculationController(CalculationService calculationService) {
        this.calculationService = calculationService;
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
}
