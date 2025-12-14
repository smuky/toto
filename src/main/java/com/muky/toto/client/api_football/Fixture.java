package com.muky.toto.client.api_football;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fixture {
    private FixtureDetails fixture;
    private League league;
    private Teams teams;
    private Goals goals;
    private Score score;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FixtureDetails {
        private long id;
        private String referee;
        private String timezone;
        private String date;
        private long timestamp;
        private Periods periods;
        private Venue venue;
        private Status status;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Periods {
        private Long first;
        private Long second;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Venue {
        private Long id;
        private String name;
        private String city;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Status {
        @JsonProperty("long")
        private String longStatus;
        @JsonProperty("short")
        private String shortStatus;
        private Long elapsed;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class League {
        private int id;
        private String name;
        private String country;
        private String logo;
        private String flag;
        private int season;
        private String round;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Teams {
        private Team home;
        private Team away;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Team {
        private int id;
        private String name;
        private String logo;
        private Boolean winner;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Goals {
        private Integer home;
        private Integer away;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Score {
        private ScoreDetail halftime;
        private ScoreDetail fulltime;
        private ScoreDetail extratime;
        private ScoreDetail penalty;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ScoreDetail {
        private Integer home;
        private Integer away;
    }
}
