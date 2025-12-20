package com.muky.toto.service.ai;

import com.muky.toto.ai_response.TodoPredictionPromptResponse;
import com.muky.toto.client.api_football.prediction.MatchAnalysisData;
import com.muky.toto.model.LeagueEnum;

public interface OpenAiService {

    String getPredictorId();

    TodoPredictionPromptResponse getTodoPredictionPromptResponse(String team1, String team2, String language,
                                                                 String extraInput, LeagueEnum leagueEnum);

    TodoPredictionPromptResponse getCleanMatchPrediction(String team1, String team2, MatchAnalysisData matchData, String language);
}
