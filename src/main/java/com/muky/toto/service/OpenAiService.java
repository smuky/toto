package com.muky.toto.service;

import com.muky.toto.ai_response.ApiFootballPredictionResponse;
import com.muky.toto.ai_response.BatchFixturePredictionResponse;
import com.muky.toto.ai_response.TodoPredictionPromptResponse;
import com.muky.toto.model.Answer;
import com.muky.toto.model.LeagueEnum;

public interface OpenAiService {
    Answer getAnswer(String team1, String team2, String language,
                     String extraInput, LeagueEnum leagueEnum);

    TodoPredictionPromptResponse getTodoPredictionPromptResponse(String team1, String team2, String language,
                                                                 String extraInput, LeagueEnum leagueEnum);

    ApiFootballPredictionResponse getApiFootballPrediction(String apiFootballPredictions, String language);

    BatchFixturePredictionResponse getBatchFixturePredictions(String fixturesPredictions, String language);
}
