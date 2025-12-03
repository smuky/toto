package com.muky.toto.service;

import com.muky.toto.config.IsraelLeagueConfig;
import com.muky.toto.config.LeagueConfig;
import com.muky.toto.model.Answer;
import com.muky.toto.model.TeamGamesEntry;
import com.muky.toto.model.TeamScoreEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@RequiredArgsConstructor
@Service
public class CalculationService {

    private final LeagueService leagueService;
    private final IsraelLeagueConfig israelLeagueConfig;
    private final OpenAiService openAiService;

    public Answer calculateAnswer(String homeTeam, String awayTeam) throws IOException {
        // Validate that both teams exist and are in the same league
        LeagueConfig leagueConfig = validateAndGetLeagueInfo(homeTeam, awayTeam);
        
        if (leagueConfig == null) {
            throw new IllegalArgumentException(
                String.format("Teams '%s' and '%s' are not found in the same league configuration", 
                    homeTeam, awayTeam)
            );
        }

        // Fetch required data
        List<TeamScoreEntry> leagueScoreBoard = leagueService.getIsraelLeagueScoreBoard(leagueConfig.getType());
        List<TeamGamesEntry> homeTeamGames = leagueService.getTeamGames(homeTeam);
        List<TeamGamesEntry> awayTeamGames = leagueService.getTeamGames(awayTeam);

        // Call OpenAI service with all required data
        Answer answer = openAiService.getAnswer(
                leagueConfig.getName(),
                homeTeam,
                awayTeam,
                "", // extraInput - can be added as parameter if needed
                leagueScoreBoard,
                homeTeamGames,
                awayTeamGames
        );
        
        return answer;
    }

    /**
     * Validates that both teams exist in the configuration and returns their league config.
     * Returns null if teams are not in the same league or don't exist.
     */
    private LeagueConfig validateAndGetLeagueInfo(String homeTeam, String awayTeam) {
        for (LeagueConfig league : israelLeagueConfig.getLeagues()) {
            if (league.getTeam() != null) {
                boolean hasHomeTeam = league.getTeam().containsValue(homeTeam);
                boolean hasAwayTeam = league.getTeam().containsValue(awayTeam);
                
                if (hasHomeTeam && hasAwayTeam) {
                    return league;
                }
            }
        }
        return null;
    }
}
