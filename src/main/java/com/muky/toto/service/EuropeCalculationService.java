package com.muky.toto.service;

import com.muky.toto.ai_response.TodoPredictionPromptResponse;
import com.muky.toto.cache.RedisCacheManager;
import com.muky.toto.client.api_football.Standing;
import com.muky.toto.model.Answer;
import com.muky.toto.model.LeagueEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class EuropeCalculationService {
    private final OpenAiService openAiService;
    private final RedisCacheManager redisCacheManager;
    private final ApiFootballService apiFootballService;

    public Answer calculateAnswer(String homeTeam, String awayTeam, String language, LeagueEnum leagueEnum) {
        Answer answer = openAiService.getAnswer(homeTeam, awayTeam, language, "", leagueEnum);
        log.info("Question {}. /n Answer: {}", homeTeam + " - " + awayTeam, answer);
        return answer;
    }

    public TodoPredictionPromptResponse calculateTotoPredictionFromStanding(String homeTeam, String awayTeam, String language, LeagueEnum leagueEnum) {
        Standing standing = apiFootballService.getStanding(leagueEnum);
        
        if (standing == null || standing.getLeague() == null || standing.getLeague().getStandings() == null) {
            throw new IllegalArgumentException("No standings found for league '" + leagueEnum + "'");
        }

        Optional<TodoPredictionPromptResponse> cachedResponse = redisCacheManager.getPrediction(homeTeam, awayTeam, language);
        if (cachedResponse.isPresent()) {
            log.info("Found cached response for {} - {} - {}", homeTeam, awayTeam, language);
            return cachedResponse.get();
        }
        
        TodoPredictionPromptResponse todoPredictionPromptResponse = openAiService.getTodoPredictionPromptResponse(homeTeam, awayTeam, language, "", leagueEnum);
        redisCacheManager.cachePrediction(homeTeam, awayTeam, language, todoPredictionPromptResponse);
        return todoPredictionPromptResponse;
    }
}
