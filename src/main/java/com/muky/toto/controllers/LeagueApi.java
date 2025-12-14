package com.muky.toto.controllers;

import com.muky.toto.controllers.response.TranslationResponse;
import com.muky.toto.model.EuropeLeagueType;
import com.muky.toto.model.IsraelLeagueType;
import com.muky.toto.model.TeamGamesEntry;
import com.muky.toto.model.TeamScoreEntry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@RequestMapping("/league")
@Tag(name = "League", description = "League management APIs")
public interface LeagueApi {

    @Operation(
            summary = "Get European league scoreboard by type",
            description = "Retrieves the current European league table from BBC Sport. Supports PREMIER_LEAGUE, SPANISH_LA_LIGA, ITALIAN_SERIE_A, and GERMAN_BUNDESLIGA league types."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved European league table"),
            @ApiResponse(responseCode = "400", description = "Invalid league type"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/europe")
    ResponseEntity<List<TeamScoreEntry>> getEuropeLeagueScoreBoard(@RequestParam EuropeLeagueType leagueType) throws IOException;

    @Deprecated
    @Operation(
            summary = "Get Israel Premier League scoreboard",
            description = "Deprecated: Use /israel endpoint instead. This endpoint retrieves the current Israel Premier League table from Sport5",
            deprecated = true
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Israel Premier League table"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/israel-premier")
    ResponseEntity<List<TeamScoreEntry>> getIsraelPremierLeagueScoreBoard() throws IOException;

    @Operation(
            summary = "Get Israel league scoreboard by type",
            description = "Retrieves the current Israel league table from Israel Football Association. Supports NATIONAL_LEAGUE and WINNER league types."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Israel league table"),
            @ApiResponse(responseCode = "400", description = "Invalid league type"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/israel")
    ResponseEntity<List<TeamScoreEntry>> getIsraelLeagueScoreBoard(@RequestParam IsraelLeagueType leagueType) throws IOException;

    @Operation(
            summary = "Get team games",
            description = "Retrieves the list of games for a specific team by name"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved team games"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/team")
    ResponseEntity<List<TeamGamesEntry>> getTeamGames(@RequestParam String name) throws IOException;

    @Operation(
            summary = "Get all teams",
            description = "Retrieves the list of all teams with league translations map"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all teams"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @GetMapping("/translations")
    ResponseEntity<TranslationResponse> getTranslations(@RequestParam(defaultValue = "en") String language);

}


