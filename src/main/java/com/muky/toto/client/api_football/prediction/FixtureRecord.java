package com.muky.toto.client.api_football.prediction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FixtureRecord {
    private VenueRecord played;
    private VenueRecord wins;
    private VenueRecord draws;
    private VenueRecord losses;
}
