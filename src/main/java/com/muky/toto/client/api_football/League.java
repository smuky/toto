package com.muky.toto.client.api_football;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class League {
    private LeagueInfo league;
    private Country country;
    private List<Season> seasons;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LeagueInfo {
        private int id;
        private String name;
        private String type;
        private String logo;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Country {
        private String name;
        private String code;
        private String flag;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Season {
        private int year;
        private String start;
        private String end;
        private boolean current;
        private Coverage coverage;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Coverage {
        private Fixtures fixtures;
        private boolean standings;
        private boolean players;
        private boolean topScorers;
        private boolean topAssists;
        private boolean topCards;
        private boolean injuries;
        private boolean predictions;
        private boolean odds;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Fixtures {
        private boolean events;
        private boolean lineups;
        private boolean statisticsFixtures;
        private boolean statisticsPlayers;
    }
}
