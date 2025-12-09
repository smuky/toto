package com.muky.toto.ai_response;
import java.util.Map;

// This record defines the schema the AI must follow
public record TodoPredictionPromptResponse(
        MatchDetails matchDetails,
        PredictionStats analysis,
        Map<String, Integer> probabilities, // Keys: "1", "X", "2", Values: Percentage
        String justification
) {
    public record MatchDetails(
            String date,
            String competition,
            String venue,
            String homeTeam,
            String awayTeam
    ) {}

    public record PredictionStats(
            String recentFormAnalysis,
            String xGAnalysis,
            String headToHeadSummary,
            String keyNews
    ) {}
}