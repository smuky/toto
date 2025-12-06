package com.muky.toto.config;

import com.muky.toto.model.IsraelLeagueType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeagueConfig {
    private IsraelLeagueType type;
    private String name;
    private Integer leagueId;
}

