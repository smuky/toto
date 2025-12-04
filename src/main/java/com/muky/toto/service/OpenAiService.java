package com.muky.toto.service;

import com.muky.toto.model.Answer;
import com.muky.toto.model.TeamGamesEntry;
import com.muky.toto.model.TeamScoreEntry;

import java.util.List;

public interface OpenAiService {
    Answer getAnswer(String leagueName, String homeTeam, String awayTeam,
                     String extraInput, List<TeamScoreEntry> scoreBoard,
                     List<TeamGamesEntry> homeTeamGames, List<TeamGamesEntry> awayTeamGames);

    Answer getEuropeLeagueAnswer(String homeTeam, String awayTeam);
}
