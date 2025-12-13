package com.muky.toto.controllers;

import com.muky.toto.client.api_football.League;
import com.muky.toto.client.api_football.Standing;
import com.muky.toto.model.apifootball.SupportedCountriesEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api-football")
@Tag(name = "API Football", description = "Football data from API-Football.com")
public interface ApiFootballApi {

    @Operation(
            summary = "Get leagues by country",
            description = "Retrieves all leagues for a specific country from API-Football (cached for 24 hours)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved leagues"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/leagues")
    ResponseEntity<List<League>> getLeagues(
            @Parameter(description = "Country", required = true)
            @RequestParam SupportedCountriesEnum country);

    @Operation(
            summary = "Get Israel Premier League standings",
            description = "Retrieves standings for Israeli Premier League for a specific season"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved standings"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/israel/standings")
    ResponseEntity<List<Standing>> getIsraelPremierLeagueStandings(
            @Parameter(description = "Season year (e.g., 2024)", required = true)
            @RequestParam int season);
}

