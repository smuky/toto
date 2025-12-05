package com.muky.toto.config;

import com.muky.toto.model.IsraelLeagueType;

import java.util.Map;

public class LeagueConfig {
    private IsraelLeagueType type;
    private String name;
    private Integer leagueId;
    private Map<String, String> team;

    public IsraelLeagueType getType() {
        return type;
    }

    public void setType(IsraelLeagueType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Integer leagueId) {
        this.leagueId = leagueId;
    }

    public Map<String, String> getTeam() {
        return team;
    }

    public void setTeam(Map<String, String> team) {
        this.team = team;
    }
}
