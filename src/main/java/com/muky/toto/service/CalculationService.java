package com.muky.toto.service;

import com.muky.toto.ai_response.TodoPredictionPromptResponse;
import com.muky.toto.cache.RedisCacheManager;
import com.muky.toto.client.api_football.Prediction;
import com.muky.toto.client.api_football.mapper.MatchAnalysisMapper;
import com.muky.toto.client.api_football.prediction.MatchAnalysisData;
import com.muky.toto.model.LeagueEnum;
import com.muky.toto.service.ai.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CalculationService {
    private final Map<String, OpenAiService> openAiServiceMap;
    private final RedisCacheManager redisCacheManager;
    private final ApiFootballService apiFootballService;
    private final MatchAnalysisMapper matchAnalysisMapper;

    public CalculationService(List<OpenAiService> openAiServices, RedisCacheManager redisCacheManager, ApiFootballService apiFootballService, MatchAnalysisMapper matchAnalysisMapper) {
        this.openAiServiceMap = openAiServices.stream()
                .collect(Collectors.toMap(OpenAiService::getPredictorId, service -> service));
        this.redisCacheManager = redisCacheManager;
        this.apiFootballService = apiFootballService;
        this.matchAnalysisMapper = matchAnalysisMapper;
    }

    public TodoPredictionPromptResponse calculateTotoPredictionFromStanding(String predictorId, String homeTeam, String awayTeam, String language, LeagueEnum leagueEnum) {
        Optional<TodoPredictionPromptResponse> cachedResponse = redisCacheManager.getPrediction(predictorId, homeTeam, awayTeam, language);
        if (cachedResponse.isPresent()) {
            log.info("Found cached response for {} - {} - {} with predictorId {}", homeTeam, awayTeam, language, predictorId);
            return cachedResponse.get();
        }
        
        OpenAiService openAiService = openAiServiceMap.get(predictorId);
        if (openAiService == null) {
            throw new IllegalArgumentException("No OpenAiService found for predictorId: " + predictorId);
        }
        
        TodoPredictionPromptResponse todoPredictionPromptResponse = openAiService.getTodoPredictionPromptResponse(homeTeam, awayTeam, language, "", leagueEnum);
        redisCacheManager.cachePrediction(predictorId, homeTeam, awayTeam, language, todoPredictionPromptResponse);
        return todoPredictionPromptResponse;
    }

    public TodoPredictionPromptResponse getPredictionFromApiFootball(String predictorId, String team1, String team2, int fixtureId, String language) {
        log.info("Getting prediction for fixture {} in language {} with predictorId {}", fixtureId, language, predictorId);
        
        Optional<TodoPredictionPromptResponse> cachedResponse = redisCacheManager.getApiFootballPrediction(predictorId, fixtureId, language);
        if (cachedResponse.isPresent()) {
            log.info("Found cached API-Football prediction for fixture {} in {} with predictorId {}", fixtureId, language, predictorId);
            return cachedResponse.get();
        }

        Prediction predictions = apiFootballService.getPredictions(fixtureId);

        if (predictions == null) {
            throw new IllegalArgumentException("No predictions found for fixture ID: " + fixtureId);
        }

        MatchAnalysisData matchAnalysisData = matchAnalysisMapper.toMatchAnalysisData(predictions);
        
        String predictionsJson = predictions.toString();
        log.debug("API-Football predictions JSON: {}", predictionsJson);

        OpenAiService openAiService = openAiServiceMap.get(predictorId);
        if (openAiService == null) {
            throw new IllegalArgumentException("No OpenAiService found for predictorId: " + predictorId);
        }

        TodoPredictionPromptResponse apiFootballPrediction = openAiService.getCleanMatchPrediction(team1, team2, matchAnalysisData, language);
        redisCacheManager.cacheApiFootballPrediction(predictorId, fixtureId, language, apiFootballPrediction);
        
        log.info("Successfully generated and cached readable prediction for fixture {} in {} with predictorId {}", fixtureId, language, predictorId);
        return apiFootballPrediction;
    }
}
