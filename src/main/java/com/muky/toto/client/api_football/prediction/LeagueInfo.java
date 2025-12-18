package com.muky.toto.client.api_football.prediction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeagueInfo {
    private Integer id;
    private String name;
    private String country;
    private Integer season;
}
