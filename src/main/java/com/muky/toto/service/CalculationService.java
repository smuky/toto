package com.muky.toto.service;

import com.muky.toto.config.IsraelLeagueConfig;
import com.muky.toto.config.LeagueConfig;
import com.muky.toto.model.Answer;
import com.muky.toto.model.LeagueType;
import com.muky.toto.model.TeamGamesEntry;
import com.muky.toto.model.TeamScoreEntry;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CalculationService {

    private final LeagueService leagueService;
    private final IsraelLeagueConfig israelLeagueConfig;

    public CalculationService(LeagueService leagueService, IsraelLeagueConfig israelLeagueConfig) {
        this.leagueService = leagueService;
        this.israelLeagueConfig = israelLeagueConfig;
    }

    public Answer calculateAnswer(String homeTeam, String awayTeam) throws IOException {
        // Validate that both teams exist and are in the same league
        LeagueType leagueType = validateAndGetLeagueType(homeTeam, awayTeam);
        
        if (leagueType == null) {
            throw new IllegalArgumentException(
                String.format("Teams '%s' and '%s' are not found in the same league configuration", 
                    homeTeam, awayTeam)
            );
        }

        // Fetch required data
        List<TeamScoreEntry> leagueScoreBoard = leagueService.getIsraelLeagueScoreBoard(leagueType);
        List<TeamGamesEntry> homeTeamGames = leagueService.getTeamGames(homeTeam);
        List<TeamGamesEntry> awayTeamGames = leagueService.getTeamGames(awayTeam);

        // TODO: Implement odds calculation logic using the fetched data
        
        return new Answer("answer");
    }

    /**
     * Validates that both teams exist in the configuration and returns their common league type.
     * Returns null if teams are not in the same league or don't exist.
     */
    private LeagueType validateAndGetLeagueType(String homeTeam, String awayTeam) {
        for (LeagueConfig league : israelLeagueConfig.getLeagues()) {
            if (league.getTeam() != null) {
                boolean hasHomeTeam = league.getTeam().containsValue(homeTeam);
                boolean hasAwayTeam = league.getTeam().containsValue(awayTeam);
                
                if (hasHomeTeam && hasAwayTeam) {
                    return league.getType();
                }
            }
        }
        return null;
    }
}
