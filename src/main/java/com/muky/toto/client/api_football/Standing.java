package com.muky.toto.client.api_football;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Standing {
    private League league;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class League {
        private int id;
        private String name;
        private String country;
        private String logo;
        private String flag;
        private int season;
        private List<List<StandingEntry>> standings;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StandingEntry {
        private int rank;
        private Team team;
        private int points;
        private int goalsDiff;
        private String group;
        private String form;
        private String status;
        private String description;
        private All all;
        private All home;
        private All away;
        private String update;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Team {
        private int id;
        private String name;
        private String logo;
        private String displayName; // Translated team name based on language
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class All {
        private int play;
        private int win;
        private int draw;
        private int lose;
        private Goals goals;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Goals {
        @JsonProperty("for")
        private int goalsFor;
        @JsonProperty("against")
        private int goalsAgainst;
    }
}
