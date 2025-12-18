package com.muky.toto.ai_response;

import java.util.Map;

public record ApiFootballPredictionResponse(
        MatchInfo matchInfo,
        PredictionSummary prediction,
        TeamAnalysis homeTeam,
        TeamAnalysis awayTeam,
        Map<String, String> probabilities, // Keys: "home", "draw", "away", Values: Percentage
        String justification
) {
    public record MatchInfo(
            String homeTeamName,
            String awayTeamName,
            String league,
            String season
    ) {}

    public record PredictionSummary(
            String winner,
            String advice,
            String expectedGoals
    ) {}

    public record TeamAnalysis(
            String recentForm,
            String last5Performance,
            String goalsAnalysis,
            String strengths
    ) {}
}
