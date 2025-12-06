package com.muky.toto.controllers;

import com.muky.toto.model.EuropeLeagueType;
import com.muky.toto.model.IsraelLeagueType;
import com.muky.toto.model.TeamGamesEntry;
import com.muky.toto.model.TeamScoreEntry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@RequestMapping("/league")
public interface LeagueApi {

    @GetMapping("/europe")
    ResponseEntity<List<TeamScoreEntry>> getEuropeLeagueScoreBoard(@RequestParam EuropeLeagueType leagueType) throws IOException;

    @Deprecated
    @GetMapping("/israel-premier")
    ResponseEntity<List<TeamScoreEntry>> getIsraelPremierLeagueScoreBoard() throws IOException;

    @GetMapping("/israel")
    ResponseEntity<List<TeamScoreEntry>> getIsraelLeagueScoreBoard(@RequestParam IsraelLeagueType leagueType) throws IOException;

    @GetMapping("/team")
    ResponseEntity<List<TeamGamesEntry>> getTeamGames(@RequestParam String name) throws IOException;

    @GetMapping("/all")
    ResponseEntity<List<TeamScoreEntry>> getAllTeams() throws IOException;


}


