package com.muky.toto.service;

import com.muky.toto.ai_response.TodoPredictionPromptResponse;
import com.muky.toto.cache.MemoryCache;
import com.muky.toto.model.Answer;
import com.muky.toto.model.LeagueEnum;
import com.muky.toto.model.TeamScoreEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class EuropeCalculationService {
    private final OpenAiService openAiService;
    private final MemoryCache memoryCache;

    public Answer calculateAnswer(String homeTeam, String awayTeam, String language) {
        TeamScoreEntry homeTeamScoreEntry = memoryCache.getTeamScoreEntry(homeTeam);
        LeagueEnum leagueEnum = homeTeamScoreEntry.getLeagueEnum();
        TeamScoreEntry awayTeamScoreEntry = memoryCache.getTeamScoreEntry(awayTeam);

        if (leagueEnum != awayTeamScoreEntry.getLeagueEnum()) {
            throw new IllegalArgumentException("Teams are not in the same league");
        }

        Answer answer = openAiService.getAnswer(homeTeam, awayTeam, language, "", leagueEnum);
        log.info("Question {}. /n Answer: {}", homeTeam + " - " + awayTeam, answer);
        return answer;
    }


    public TodoPredictionPromptResponse calculateTotoPrediction(String homeTeam, String awayTeam, String language) {
        TeamScoreEntry homeTeamScoreEntry = memoryCache.getTeamScoreEntry(homeTeam);
        LeagueEnum leagueEnum = homeTeamScoreEntry.getLeagueEnum();
        TeamScoreEntry awayTeamScoreEntry = memoryCache.getTeamScoreEntry(awayTeam);

        if (leagueEnum != awayTeamScoreEntry.getLeagueEnum()) {
            throw new IllegalArgumentException("Teams are not in the same league");
        }

        TodoPredictionPromptResponse todoPredictionPromptResponse = openAiService.getTodoPredictionPromptResponse(homeTeam, awayTeam, language, "", leagueEnum);
        log.info("Question {}. /n Answer: {}", homeTeam + " - " + awayTeam, todoPredictionPromptResponse);
        return todoPredictionPromptResponse;
    }
}
