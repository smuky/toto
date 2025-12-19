package com.muky.toto.controllers;

import com.muky.toto.ai_response.TodoPredictionPromptResponse;
import com.muky.toto.model.LeagueEnum;
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

@RequestMapping("/calculation")
@Tag(name = "Calculation", description = "Odds calculation APIs")
public interface CalculationApi {

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
            @RequestParam(value = "predictorId") String predictorId,
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
}
