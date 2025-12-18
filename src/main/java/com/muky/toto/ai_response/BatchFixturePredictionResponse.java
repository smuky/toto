package com.muky.toto.ai_response;

import java.util.List;

public record BatchFixturePredictionResponse(
        List<FixturePrediction> predictions
) {
    public record FixturePrediction(
            String homeTeam,
            String awayTeam,
            String result
    ) {}
}
