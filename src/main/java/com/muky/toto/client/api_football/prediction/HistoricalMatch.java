package com.muky.toto.client.api_football.prediction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalMatch {
    private String date;
    private String season;
    private String round;
    private String venue;
    private TeamResult homeTeam;
    private TeamResult awayTeam;
}
