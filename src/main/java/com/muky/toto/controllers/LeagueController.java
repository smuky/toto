package com.muky.toto.controllers;

import com.muky.toto.model.LeagueType;
import com.muky.toto.model.TeamGamesEntry;
import com.muky.toto.model.TeamScoreEntry;
import com.muky.toto.service.LeagueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class LeagueController implements LeagueApi {

    private final LeagueService leagueService;

    public LeagueController(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @Override
    public ResponseEntity<List<TeamScoreEntry>> getEnglandPremierLeague() throws IOException {
        List<TeamScoreEntry> leagueTable = leagueService.getEnglandPremierLeague();
        return ResponseEntity.ok(leagueTable);
    }

    @Override
    public ResponseEntity<List<TeamScoreEntry>> getIsraelPremierLeagueScoreBoard() throws IOException {
        List<TeamScoreEntry> israelLeagueTable = leagueService.getIsraelPremierLeagueScoreBoard();
        return ResponseEntity.ok(israelLeagueTable);
    }

    @Override
    public ResponseEntity<List<TeamScoreEntry>> getIsraelLeagueScoreBoard(LeagueType leagueType) throws IOException {
        List<TeamScoreEntry> israelLeagueTable = leagueService.getIsraelLeagueScoreBoard(leagueType);
        return ResponseEntity.ok(israelLeagueTable);
    }

    @Override
    public ResponseEntity<List<TeamGamesEntry>> getTeamGames(String name) throws IOException {
        List<TeamGamesEntry> teamGames = leagueService.getTeamGames(name);
        return ResponseEntity.ok(teamGames);
    }


}
