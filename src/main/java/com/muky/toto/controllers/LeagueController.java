package com.muky.toto.controllers;

import com.muky.toto.controllers.response.TranslationResponse;
import com.muky.toto.model.EuropeLeagueType;
import com.muky.toto.model.IsraelLeagueType;
import com.muky.toto.model.TeamGamesEntry;
import com.muky.toto.model.TeamScoreEntry;
import com.muky.toto.service.ApiFootballService;
import com.muky.toto.service.LeagueService;
import com.muky.toto.service.TranslationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class LeagueController implements LeagueApi {

    private final LeagueService leagueService;
    private final TranslationService translationService;

    public LeagueController(LeagueService leagueService, TranslationService translationService) {
        this.leagueService = leagueService;
        this.translationService = translationService;
    }

    @Override
    public ResponseEntity<List<TeamScoreEntry>> getEuropeLeagueScoreBoard(EuropeLeagueType leagueType) throws IOException {
        List<TeamScoreEntry> leagueTable = leagueService.getEuropeLeagueScoreBoard(leagueType);
        return ResponseEntity.ok(leagueTable);
    }

    @Override
    public ResponseEntity<List<TeamScoreEntry>> getIsraelPremierLeagueScoreBoard() throws IOException {
        List<TeamScoreEntry> israelLeagueTable = leagueService.getIsraelPremierLeagueScoreBoard();
        return ResponseEntity.ok(israelLeagueTable);
    }

    @Override
    public ResponseEntity<List<TeamScoreEntry>> getIsraelLeagueScoreBoard(IsraelLeagueType leagueType) throws IOException {
        List<TeamScoreEntry> israelLeagueTable = leagueService.getIsraelLeagueScoreBoard(leagueType);
        return ResponseEntity.ok(israelLeagueTable);
    }

    @Override
    public ResponseEntity<List<TeamGamesEntry>> getTeamGames(String name) throws IOException {
        List<TeamGamesEntry> teamGames = leagueService.getTeamGames(name);
        return ResponseEntity.ok(teamGames);
    }

    @Override
    public ResponseEntity<TranslationResponse> getTranslations(String language) {
        TranslationResponse translations = translationService.getTranslations(language);
        return ResponseEntity.ok(translations);
    }
}
