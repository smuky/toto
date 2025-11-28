package com.muky.toto.config;

import com.muky.toto.model.LeagueType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@ConfigurationProperties(prefix = "app.league.israel")
public class IsraelLeagueConfig {
    private Integer seasonId;
    private List<LeagueConfig> leagues;

    public Integer getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Integer seasonId) {
        this.seasonId = seasonId;
    }

    public List<LeagueConfig> getLeagues() {
        return leagues;
    }

    public void setLeagues(List<LeagueConfig> leagues) {
        this.leagues = leagues;
    }

    /**
     * Get league configuration by type
     */
    public LeagueConfig getLeagueByType(LeagueType type) {
        return leagues.stream()
                .filter(league -> league.getType() == type)
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all leagues as a map indexed by type
     */
    public Map<LeagueType, LeagueConfig> getLeaguesMap() {
        return leagues.stream()
                .collect(Collectors.toMap(LeagueConfig::getType, league -> league));
    }
}
