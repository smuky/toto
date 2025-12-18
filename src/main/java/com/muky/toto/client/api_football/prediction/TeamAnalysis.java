package com.muky.toto.client.api_football.prediction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamAnalysis {
    private Integer id;
    private String name;
    private SeasonStats seasonStats;
    private RecentFormStats recentForm;
}
