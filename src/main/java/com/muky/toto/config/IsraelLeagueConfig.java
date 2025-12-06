package com.muky.toto.config;

import com.muky.toto.model.IsraelLeagueType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.league.israel")
public class IsraelLeagueConfig {
    private Integer seasonId;
    private List<LeagueConfig> leagues;

    /**
     * Get league configuration by type
     */
    public LeagueConfig getLeagueByType(IsraelLeagueType type) {
        return leagues.stream()
                .filter(league -> league.getType() == type)
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all leagues as a map indexed by type
     */
    public Map<IsraelLeagueType, LeagueConfig> getLeaguesMap() {
        return leagues.stream()
                .collect(Collectors.toMap(LeagueConfig::getType, league -> league));
    }
}
