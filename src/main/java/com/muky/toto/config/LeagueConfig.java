package com.muky.toto.config;

import com.muky.toto.model.LeagueType;

import java.util.Map;

public class LeagueConfig {
    private LeagueType type;
    private String name;
    private Integer leagueId;
    private Map<String, String> team;

    public LeagueType getType() {
        return type;
    }

    public void setType(LeagueType type) {
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
