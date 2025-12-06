package com.muky.toto.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/calculation")
@Tag(name = "Calculation", description = "Odds calculation APIs")
public interface CalculationApi {

    @Operation(
            summary = "Calculate match odds",
            description = "Calculates the odds for a match between two teams"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated odds"),
            @ApiResponse(responseCode = "400", description = "Invalid team names"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/calculate-odds")
    ResponseEntity<String> calculateOdds(
            @Parameter(description = "Home team name", required = true)
            @RequestParam("home-team") String homeTeam,
            @Parameter(description = "Away team name", required = true)
            @RequestParam("away-team") String awayTeam,
            @Parameter(description = "Language")
            @RequestParam("language") String language
    );
}
