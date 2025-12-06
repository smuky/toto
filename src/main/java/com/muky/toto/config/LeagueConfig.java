package com.muky.toto.config;

import com.muky.toto.model.IsraelLeagueType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class LeagueConfig {
    private IsraelLeagueType type;
    private String name;
    private Integer leagueId;
    private Map<String, String> team;
}

