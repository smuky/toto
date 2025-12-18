package com.muky.toto.client.api_football.prediction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamResult {
    private Integer id;
    private String name;
    private Integer goals;
}
