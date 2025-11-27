package com.muky.toto.controllers;

import com.muky.toto.model.TeamScoreEntry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@RequestMapping("/league")
@Tag(name = "League", description = "League management APIs")
public interface LeagueApi {

    @Operation(
            summary = "Get Premier League table",
            description = "Retrieves the current Premier League table from BBC Sport"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Premier League table"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    ResponseEntity<List<TeamScoreEntry>> getLeague() throws IOException;

    @Operation(
            summary = "Get Israel Premier League scoreboard",
            description = "Retrieves the current Israel Premier League table from Sport5"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Israel Premier League table"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/israel")
    ResponseEntity<List<TeamScoreEntry>> getIsraelPremierLeagueScoreBoard() throws IOException;
}
