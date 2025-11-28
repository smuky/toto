package com.muky.toto.config;

import java.util.Map;

public class NationalLeagueConfig {
    private Integer leagueId;
    private Map<Integer, String> team;

    public Integer getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Integer leagueId) {
        this.leagueId = leagueId;
    }

    public Map<Integer, String> getTeam() {
        return team;
    }

    public void setTeam(Map<Integer, String> team) {
        this.team = team;
    }
}
