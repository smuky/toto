package com.muky.toto.service;

import com.muky.toto.ai_response.TodoPredictionPromptResponse;
import com.muky.toto.cache.MemoryCache;
import com.muky.toto.cache.RedisCacheManager;
import com.muky.toto.model.Answer;
import com.muky.toto.model.LeagueEnum;
import com.muky.toto.model.TeamScoreEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class EuropeCalculationService {
    private final OpenAiService openAiService;
    private final MemoryCache memoryCache;
    private final RedisCacheManager redisCacheManager;

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
        Optional<TodoPredictionPromptResponse> cachedResponse = redisCacheManager.getPrediction(homeTeam, awayTeam, language);
            if (cachedResponse.isPresent()) {
                try {
                    log.info("Found cached response for {} - {} - {}", homeTeam, awayTeam, language);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    log.error("Error during sleep that simulate latency", e);
                }
                return cachedResponse.get();
            }
/*
            if (!language.equals(SupportedLanguageEnum.ENGLISH)) {
                cachedResponse = redisCacheManager.getPrediction(homeTeam, awayTeam, SupportedLanguageEnum.ENGLISH);
            }
            if (cachedResponse.isPresent()) {

                return cachedResponse.get();
            }
*/
            TodoPredictionPromptResponse todoPredictionPromptResponse = openAiService.getTodoPredictionPromptResponse(homeTeam, awayTeam, language, "", leagueEnum);
            redisCacheManager.cachePrediction(homeTeam, awayTeam, language, todoPredictionPromptResponse);
            return todoPredictionPromptResponse;
    }
}
