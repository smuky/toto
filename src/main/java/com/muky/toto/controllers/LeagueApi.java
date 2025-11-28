package com.muky.toto.controllers;

import com.muky.toto.model.LeagueType;
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
            summary = "Get England Premier League table",
            description = "Retrieves the current Premier League table from BBC Sport"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Premier League table"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/england-premier-league")
    ResponseEntity<List<TeamScoreEntry>> getEnglandPremierLeague() throws IOException;

    @Operation(
            summary = "Get Israel Premier League scoreboard",
            description = "Retrieves the current Israel Premier League table from Sport5"
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
    ResponseEntity<List<TeamScoreEntry>> getIsraelLeagueScoreBoard(@RequestParam LeagueType leagueType) throws IOException;

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
}
