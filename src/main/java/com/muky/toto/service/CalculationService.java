package com.muky.toto.service;

import com.muky.toto.ai_response.TodoPredictionPromptResponse;
import com.muky.toto.cache.RedisCacheManager;
import com.muky.toto.client.api_football.Prediction;
import com.muky.toto.client.api_football.Standing;
import com.muky.toto.client.api_football.mapper.MatchAnalysisMapper;
import com.muky.toto.client.api_football.prediction.MatchAnalysisData;
import com.muky.toto.model.Answer;
import com.muky.toto.model.LeagueEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CalculationService {
    private final OpenAiService openAiService;
    private final RedisCacheManager redisCacheManager;
    private final ApiFootballService apiFootballService;
    private final MatchAnalysisMapper matchAnalysisMapper;

    public Answer calculateAnswer(String homeTeam, String awayTeam, String language, LeagueEnum leagueEnum) {
        Answer answer = openAiService.getAnswer(homeTeam, awayTeam, language, "", leagueEnum);
        log.info("Question {}. /n Answer: {}", homeTeam + " - " + awayTeam, answer);
        return answer;
    }

    public TodoPredictionPromptResponse calculateTotoPredictionFromStanding(String predictorId, String homeTeam, String awayTeam, String language, LeagueEnum leagueEnum) {
        Optional<TodoPredictionPromptResponse> cachedResponse = redisCacheManager.getPrediction(predictorId, homeTeam, awayTeam, language);
        if (cachedResponse.isPresent()) {
            log.info("Found cached response for {} - {} - {} with predictorId {}", homeTeam, awayTeam, language, predictorId);
            return cachedResponse.get();
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

        TodoPredictionPromptResponse apiFootballPrediction = openAiService.getCleanMatchPrediction(team1, team2, matchAnalysisData, language);
        redisCacheManager.cacheApiFootballPrediction(predictorId, fixtureId, language, apiFootballPrediction);
        
        log.info("Successfully generated and cached readable prediction for fixture {} in {} with predictorId {}", fixtureId, language, predictorId);
        return apiFootballPrediction;
    }

 /*   public BatchFixturePredictionResponse getBatchFixturePredictions(List<Integer> fixtureIds, String language) {
        log.info("Getting batch predictions for {} fixtures in language {}", fixtureIds.size(), language);
        
        StringBuilder allPredictionsBuilder = new StringBuilder();
        allPredictionsBuilder.append("[");
        
        for (int i = 0; i < fixtureIds.size(); i++) {
            int fixtureId = fixtureIds.get(i);
            log.info("Fetching prediction data for fixture {}", fixtureId);
            
            JsonNode predictions = apiFootballService.getPredictions(fixtureId);
            
            if (predictions == null) {
                log.warn("No predictions found for fixture ID: {}, skipping", fixtureId);
                continue;
            }
            
            allPredictionsBuilder.append(predictions.toString());
            
            if (i < fixtureIds.size() - 1) {
                allPredictionsBuilder.append(",");
            }
        }
        
        allPredictionsBuilder.append("]");
        
        String allPredictionsJson = allPredictionsBuilder.toString();
        log.debug("Combined predictions JSON for {} fixtures", fixtureIds.size());
        
        BatchFixturePredictionResponse batchPrediction = openAiService.getBatchFixturePredictions(allPredictionsJson, language);
        
        log.info("Successfully generated batch predictions for {} fixtures in {}", fixtureIds.size(), language);
        return batchPrediction;
    }*/
}
