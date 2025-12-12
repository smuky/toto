package com.muky.toto.controllers;

import com.muky.toto.ai_response.TodoPredictionPromptResponse;
import com.muky.toto.model.LeagueEnum;
import com.muky.toto.model.SupportedLanguageEnum;
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
            @RequestParam("language") String language,
            @Parameter(description = "League", required = true)
            @RequestParam("league") LeagueEnum league
    );

    @Operation(
            summary = "Calculate match prediction with structured response",
            description = "Calculates detailed match prediction with probabilities, analysis, and justification in a structured format"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated prediction"),
            @ApiResponse(responseCode = "400", description = "Invalid team names or teams not in same league"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/calculate-prediction")
    ResponseEntity<TodoPredictionPromptResponse> calculatePrediction(
            @Parameter(description = "Home team name", required = true)
            @RequestParam("home-team") String homeTeam,
            @Parameter(description = "Away team name", required = true)
            @RequestParam("away-team") String awayTeam,
            @Parameter(description = "Language for analysis text (default: hebrew)")
            @RequestParam(value = "language", defaultValue = "hebrew") SupportedLanguageEnum language,
            @Parameter(description = "League", required = true)
            @RequestParam("league") LeagueEnum league
    );
}
