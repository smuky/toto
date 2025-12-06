package com.muky.toto.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/calculation")
public interface CalculationApi {

    @GetMapping("/calculate-odds")
    ResponseEntity<String> calculateOdds(
            @RequestParam("home-team") String homeTeam,
            @RequestParam("away-team") String awayTeam
    );

    @GetMapping("/calculate-europe-odds")
    ResponseEntity<String> calculateEuropeLeagueOdds(
            @RequestParam("home-team") String homeTeam,
            @RequestParam("away-team") String awayTeam
    );
}
