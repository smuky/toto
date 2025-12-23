package com.muky.toto.controllers;

import com.muky.toto.client.api_football.Fixture;
import com.muky.toto.controllers.requests.WinnerGamesRequest;
import com.muky.toto.controllers.response.VersionResponse;
import com.muky.toto.controllers.response.WinnerGameResponse;
import com.muky.toto.model.LeagueEnum;
import com.muky.toto.service.ApiFootballService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/version")
@Tag(name = "Version", description = "App version APIs")
public class VersionController {

    @Value("${app.min-version:1.0.0+25}")
    private String appMinVersion;
    
    private final ApiFootballService apiFootballService;
    private final ApiFootballController apiFootballController;
    
    public VersionController(ApiFootballService apiFootballService, ApiFootballController apiFootballController) {
        this.apiFootballService = apiFootballService;
        this.apiFootballController = apiFootballController;
    }

    @Operation(
            summary = "Get minimum app version",
            description = "Returns the minimum required app version for the client"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved minimum version")
    })
    @GetMapping
    public ResponseEntity<VersionResponse> getMinVersion() {
        log.info("Version check requested. Returning minimum version: {}", appMinVersion);
        VersionResponse response = new VersionResponse(appMinVersion);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get games by draw number",
            description = "Retrieves all games matching the specified draw number from the provided games data"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved games"),
            @ApiResponse(responseCode = "404", description = "No games found for the specified draw number")
    })
    @PostMapping("/games")
    public ResponseEntity<List<WinnerGameResponse>> getGamesByDrawNumber(
            @Parameter(description = "Draw number to filter games", required = true)
            @RequestParam int drawNumber,
            @Parameter(description = "Games data JSON", required = true)
            @RequestBody WinnerGamesRequest request) {
        
        log.info("Fetching games for drawNumber: {}", drawNumber);
        
        List<WinnerGameResponse> games = request.getGames().stream()
                .filter(game -> game.getDrawNumber() == drawNumber)
                .flatMap(game -> game.getRows().stream())
                .map(row -> new WinnerGameResponse(
                        row.getRowNumber(),
                        row.getLeague(),
                        row.getTeamA(),
                        row.getTeamB()
                ))
                .collect(Collectors.toList());
        
        if (games.isEmpty()) {
            log.warn("No games found for drawNumber: {}", drawNumber);
            return ResponseEntity.notFound().build();
        }
        
        log.info("Found {} games for drawNumber: {}", games.size(), drawNumber);
        return ResponseEntity.ok(games);
    }

    //body is taken from postman POST on https://www.winner.co.il/api/v2/publicapi/GetTotoDraws
    @Operation(
            summary = "Get fixture IDs by draw number",
            description = "Retrieves fixture IDs for games matching the specified draw number, returns concatenated fixture IDs"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved fixture IDs"),
            @ApiResponse(responseCode = "404", description = "No games found for the specified draw number")
    })
    @PostMapping("/games/fixtures")
    public ResponseEntity<String> getFixtureIdsByDrawNumber(
            @Parameter(description = "Draw number to filter games", required = true)
            @RequestParam int drawNumber,
            @Parameter(description = "Draw number to filter games", required = true)
            @RequestParam int gameType,
            @Parameter(description = "Games data JSON", required = true)
            @RequestBody WinnerGamesRequest request) {
        
        log.info("Fetching fixture IDs for drawNumber: {}", drawNumber);
        
        List<WinnerGamesRequest.Row> games = request.getGames().stream()
                .filter(game -> game.getDrawNumber() == drawNumber & game.getGameType() == gameType)
                .flatMap(game -> game.getRows().stream())
                .collect(Collectors.toList());
        
        if (games.isEmpty()) {
            log.warn("No games found for drawNumber: {}", drawNumber);
            return ResponseEntity.notFound().build();
        }
        
        Map<LeagueEnum, List<Fixture>> fixturesByLeague = new HashMap<>();
        Set<String> uniqueLeagues = games.stream()
                .map(WinnerGamesRequest.Row::getLeague)
                .collect(Collectors.toSet());
        
        for (String leagueName : uniqueLeagues) {
            LeagueEnum leagueEnum = LeagueEnum.fromWinnerLeagueName(leagueName);
            if (leagueEnum != null && !fixturesByLeague.containsKey(leagueEnum)) {
                log.info("Fetching fixtures for league: {} ({})", leagueName, leagueEnum);
                List<Fixture> fixtures = apiFootballService.getNextFixtures(leagueEnum, 30);
                apiFootballController.populateFixtureTeamDisplayNames(fixtures, "he");
                fixturesByLeague.put(leagueEnum, fixtures);
                log.info("Fetched {} fixtures for league: {}", fixtures.size(), leagueEnum);
            }
        }
        
        List<Long> fixtureIds = new ArrayList<>();
        
        for (WinnerGamesRequest.Row game : games) {
            LeagueEnum leagueEnum = LeagueEnum.fromWinnerLeagueName(game.getLeague());
            if (leagueEnum == null) {
                log.warn("Could not identify league for: {}", game.getLeague());
                continue;
            }
            
            List<Fixture> fixtures = fixturesByLeague.get(leagueEnum);
            if (fixtures == null) {
                log.warn("No fixtures found for league: {}", leagueEnum);
                continue;
            }
            
            Optional<Fixture> matchedFixture = fixtures.stream()
                    .filter(fixture -> fixture.getTeams() != null)
                    .filter(fixture -> {
                        String homeDisplayName = fixture.getTeams().getHome() != null ? 
                                fixture.getTeams().getHome().getDisplayName() : null;
                        String awayDisplayName = fixture.getTeams().getAway() != null ? 
                                fixture.getTeams().getAway().getDisplayName() : null;
                        
                        boolean homeMatch = homeDisplayName != null && homeDisplayName.equals(game.getTeamA());
                        boolean awayMatch = awayDisplayName != null && awayDisplayName.equals(game.getTeamB());
                        
                        return homeMatch && awayMatch;
                    })
                    .findFirst();
            
            if (matchedFixture.isPresent()) {
                long fixtureId = matchedFixture.get().getFixture().getId();
                fixtureIds.add(fixtureId);
                log.info("Matched game: {} vs {} -> fixture ID: {}", 
                        game.getTeamA(), game.getTeamB(), fixtureId);
            } else {
                log.warn("Could not find fixture for game: {} vs {} in league: {}", 
                        game.getTeamA(), game.getTeamB(), game.getLeague());
            }
        }
        
        String result = fixtureIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("-"));
        
        log.info("Returning {} fixture IDs for drawNumber {}: {}", fixtureIds.size(), drawNumber, result);
        return ResponseEntity.ok(result);
    }
}
