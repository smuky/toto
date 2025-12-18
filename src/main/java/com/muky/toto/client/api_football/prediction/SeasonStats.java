package com.muky.toto.client.api_football.prediction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeasonStats {
    private String formString;
    private FixtureRecord fixtures;
    private GoalRecord goals;
    private VenueRecord cleanSheets;
    private VenueRecord failedToScore;
    private StreakRecord biggestStreak;
}
