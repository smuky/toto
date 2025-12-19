package com.muky.toto.controllers;

import com.muky.toto.ai_response.ApiFootballPredictionResponse;
import com.muky.toto.ai_response.BatchFixturePredictionResponse;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
            summary = "Get prediction from API-Football fixture",
            description = "Retrieves predictions from API-Football for a specific fixture and formats them in a readable user-friendly format using AI"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved and formatted prediction"),
            @ApiResponse(responseCode = "400", description = "Invalid fixture ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/calculate-prediction")
    ResponseEntity<TodoPredictionPromptResponse> calculatePrediction(
            @Parameter(description = "Predictor ID", required = true)
            @RequestParam(value = "predictor-id") String predictorId,
            @Parameter(description = "Home team name", required = true)
            @RequestParam("home-team") String team1,
            @Parameter(description = "Away team name", required = true)
            @RequestParam("away-team") String team2,
            @Parameter(description = "Fixture ID from API-Football", required = true)
            @RequestParam("fixtureId") int fixtureId,
            @Parameter(description = "League", required = true)
            @RequestParam("league") LeagueEnum league,
            @Parameter(description = "Language code from Accept-Language header (e.g., 'en', 'he')")
            @RequestHeader(value = "Accept-Language", defaultValue = "en") String language
    );

    @Operation(
            summary = "Get batch predictions for multiple fixtures",
            description = "Retrieves predictions from API-Football for multiple fixtures and formats them into concise summaries using AI"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved and formatted batch predictions"),
            @ApiResponse(responseCode = "400", description = "Invalid fixture IDs"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/batch-predictions")
    ResponseEntity<BatchFixturePredictionResponse> getBatchFixturePredictions(
            @Parameter(description = "List of fixture IDs from API-Football", required = true)
            @RequestParam("fixtures") List<Integer> fixtureIds,
            @Parameter(description = "Language code from Accept-Language header (e.g., 'en', 'he')")
            @RequestHeader(value = "Accept-Language", defaultValue = "en") String language
    );
}
