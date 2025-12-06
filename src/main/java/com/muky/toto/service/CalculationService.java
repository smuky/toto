package com.muky.toto.service;

import com.muky.toto.config.IsraelLeagueConfig;
import com.muky.toto.model.Answer;
import com.muky.toto.model.LeagueEnum;
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
        TeamScoreEntry homeTeamStats = leagueService.getTeamScore(homeTeam);
        LeagueEnum leagueEnum = homeTeamStats.getLeagueEnum();

        TeamScoreEntry awayTeamStats = leagueService.getTeamScore(awayTeam);
        if (leagueEnum != awayTeamStats.getLeagueEnum()) {
            throw new IllegalArgumentException(
                    String.format("Teams '%s' and '%s' are not found in the same league configuration",
                            homeTeam, awayTeam)
            );
        }

        List<TeamScoreEntry> leagueScoreBoard = leagueService.getLeagueScoreBoard(leagueEnum);

        List<TeamGamesEntry> homeTeamGames = null;
        List<TeamGamesEntry> awayTeamGames = null;
        switch (leagueEnum) {
            case ISRAEL_WINNER:
            case ISRAEL_NATIONAL_LEAGUE:
                homeTeamGames = leagueService.getTeamGames(homeTeam);
                awayTeamGames = leagueService.getTeamGames(awayTeam);
                break;
            default:
                break;

        }

        // Call OpenAI service with all required data
        Answer answer = openAiService.getAnswer(
                leagueEnum.name(),
                homeTeam,
                awayTeam,
                "", // extraInput - can be added as parameter if needed
                leagueScoreBoard,
                homeTeamGames,
                awayTeamGames
        );
        
        return answer;
    }
}
