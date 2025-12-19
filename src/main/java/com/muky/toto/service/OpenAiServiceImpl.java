package com.muky.toto.service;

import com.muky.toto.ai_response.TodoPredictionPromptResponse;
import com.muky.toto.client.api_football.prediction.MatchAnalysisData;
import com.muky.toto.model.LeagueEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
//@Service
@RequiredArgsConstructor
public class OpenAiServiceImpl implements OpenAiService {

    @Override
    public TodoPredictionPromptResponse getTodoPredictionPromptResponse(String team1, String team2, String language, String extraInput, LeagueEnum leagueEnum) {
        return null;
    }

    @Override
    public TodoPredictionPromptResponse getCleanMatchPrediction(String team1, String team2, MatchAnalysisData matchData, String language) {
        return null;
    }

}
