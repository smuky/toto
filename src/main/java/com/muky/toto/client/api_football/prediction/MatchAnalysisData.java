package com.muky.toto.client.api_football.prediction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchAnalysisData {
    private LeagueInfo league;
    private TeamAnalysis homeTeam;
    private TeamAnalysis awayTeam;
    private List<HistoricalMatch> headToHead;
}
