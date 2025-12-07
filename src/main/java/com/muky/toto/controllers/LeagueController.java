package com.muky.toto.controllers;

import com.muky.toto.cache.MemoryCache;
import com.muky.toto.controllers.response.AllTeamsResponse;
import com.muky.toto.controllers.response.TranslationResponse;
import com.muky.toto.model.EuropeLeagueType;
import com.muky.toto.model.IsraelLeagueType;
import com.muky.toto.model.LeagueEnum;
import com.muky.toto.model.TeamGamesEntry;
import com.muky.toto.model.TeamScoreEntry;
import com.muky.toto.service.LeagueService;
import com.muky.toto.service.TranslationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class LeagueController implements LeagueApi {

    private final LeagueService leagueService;
    private final MemoryCache memoryCache;
    private final TranslationService translationService;

    public LeagueController(LeagueService leagueService, MemoryCache memoryCache, TranslationService translationService) {
        this.leagueService = leagueService;
        this.memoryCache = memoryCache;
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
    public ResponseEntity<AllTeamsResponse> getAllTeams(String language) {
        List<TeamScoreEntry> allTeams = memoryCache.getAllTeams();
        
        Map<LeagueEnum, String> leagueTranslations = Arrays.stream(LeagueEnum.values())
                .collect(Collectors.toMap(
                        league -> league,
                        league -> translationService.getLeagueName(league, language)
                ));
        
        Map<String, String> languageTranslations = Map.of(
                "en", translationService.translate("language.english", language),
                "es", translationService.translate("language.spanish", language),
                "it", translationService.translate("language.italian", language),
                "de", translationService.translate("language.german", language),
                "he", translationService.translate("language.hebrew", language)
        );
        
        String selectLeague = translationService.translate("select.league", language);
        String settings = translationService.translate("settings", language);
        String about = translationService.translate("about", language);
        
        TranslationResponse translations = new TranslationResponse(
                leagueTranslations,
                languageTranslations,
                selectLeague,
                settings,
                about
        );
        
        return ResponseEntity.ok(new AllTeamsResponse(allTeams, translations));
    }


}
