package com.muky.toto.client.api_football;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Prediction {
    private PredictionDetails predictions;
    private League league;
    private Teams teams;
    private Comparison comparison;
    private List<Fixture> h2h;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PredictionDetails {
        private Winner winner;
        @JsonProperty("win_or_draw")
        private Boolean winOrDraw;
        @JsonProperty("under_over")
        private String underOver;
        private Goals goals;
        private String advice;
        private Percent percent;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Winner {
        private Integer id;
        private String name;
        private String comment;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Goals {
        private String home;
        private String away;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Percent {
        private String home;
        private String draw;
        private String away;
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
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Teams {
        private TeamDetails home;
        private TeamDetails away;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TeamDetails {
        private int id;
        private String name;
        private String logo;
        @JsonProperty("last_5")
        private Last5 last5;
        private LeagueStats league;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Last5 {
        private int played;
        private String form;
        private String att;
        private String def;
        private Last5Goals goals;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Last5Goals {
        @JsonProperty("for")
        private GoalStats forGoals;
        private GoalStats against;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoalStats {
        private Integer total;
        private String average;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LeagueStats {
        private String form;
        private FixtureStats fixtures;
        private LeagueGoals goals;
        private Biggest biggest;
        @JsonProperty("clean_sheet")
        private HomeAwayTotal cleanSheet;
        @JsonProperty("failed_to_score")
        private HomeAwayTotal failedToScore;
        private Penalty penalty;
        private List<Lineup> lineups;
        private Cards cards;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FixtureStats {
        private HomeAwayTotal played;
        private HomeAwayTotal wins;
        private HomeAwayTotal draws;
        private HomeAwayTotal loses;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HomeAwayTotal {
        private Integer home;
        private Integer away;
        private Integer total;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LeagueGoals {
        @JsonProperty("for")
        private DetailedGoals forGoals;
        private DetailedGoals against;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DetailedGoals {
        private HomeAwayTotal total;
        private HomeAwayAverage average;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HomeAwayAverage {
        private String home;
        private String away;
        private String total;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Biggest {
        private Streak streak;
        private HomeAwayString wins;
        private HomeAwayString loses;
        private BiggestGoals goals;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Streak {
        private Integer wins;
        private Integer draws;
        private Integer loses;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HomeAwayString {
        private String home;
        private String away;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BiggestGoals {
        @JsonProperty("for")
        private HomeAwayInt forGoals;
        private HomeAwayInt against;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HomeAwayInt {
        private Integer home;
        private Integer away;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Penalty {
        private PenaltyStats scored;
        private PenaltyStats missed;
        private Integer total;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PenaltyStats {
        private Integer total;
        private String percentage;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Lineup {
        private String formation;
        private Integer played;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cards {
        private CardStats yellow;
        private CardStats red;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CardStats {
        @JsonProperty("0-15")
        private TimeSlot slot0_15;
        @JsonProperty("16-30")
        private TimeSlot slot16_30;
        @JsonProperty("31-45")
        private TimeSlot slot31_45;
        @JsonProperty("46-60")
        private TimeSlot slot46_60;
        @JsonProperty("61-75")
        private TimeSlot slot61_75;
        @JsonProperty("76-90")
        private TimeSlot slot76_90;
        @JsonProperty("91-105")
        private TimeSlot slot91_105;
        @JsonProperty("106-120")
        private TimeSlot slot106_120;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TimeSlot {
        private Integer total;
        private String percentage;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Comparison {
        private ComparisonPercent form;
        private ComparisonPercent att;
        private ComparisonPercent def;
        @JsonProperty("poisson_distribution")
        private ComparisonPercent poissonDistribution;
        private ComparisonPercent h2h;
        private ComparisonPercent goals;
        private ComparisonPercent total;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ComparisonPercent {
        private String home;
        private String away;
    }
}
