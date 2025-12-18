package com.muky.toto.client.api_football.mapper;

import com.muky.toto.client.api_football.Fixture;
import com.muky.toto.client.api_football.Prediction;
import com.muky.toto.client.api_football.prediction.FixtureRecord;
import com.muky.toto.client.api_football.prediction.GoalDetails;
import com.muky.toto.client.api_football.prediction.GoalRecord;
import com.muky.toto.client.api_football.prediction.HistoricalMatch;
import com.muky.toto.client.api_football.prediction.LeagueInfo;
import com.muky.toto.client.api_football.prediction.MatchAnalysisData;
import com.muky.toto.client.api_football.prediction.RecentFormStats;
import com.muky.toto.client.api_football.prediction.SeasonStats;
import com.muky.toto.client.api_football.prediction.SimpleGoalStats;
import com.muky.toto.client.api_football.prediction.StreakRecord;
import com.muky.toto.client.api_football.prediction.TeamAnalysis;
import com.muky.toto.client.api_football.prediction.TeamResult;
import com.muky.toto.client.api_football.prediction.VenueAverage;
import com.muky.toto.client.api_football.prediction.VenueRecord;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MatchAnalysisMapper {

    public MatchAnalysisData toMatchAnalysisData(Prediction prediction) {
        if (prediction == null) {
            return null;
        }

        return MatchAnalysisData.builder()
                .league(mapLeagueInfo(prediction.getLeague()))
                .homeTeam(mapTeamAnalysis(prediction.getTeams().getHome()))
                .awayTeam(mapTeamAnalysis(prediction.getTeams().getAway()))
                .headToHead(mapHeadToHead(prediction.getH2h()))
                .build();
    }

    private LeagueInfo mapLeagueInfo(Prediction.League league) {
        if (league == null) {
            return null;
        }

        return LeagueInfo.builder()
                .id(league.getId())
                .name(league.getName())
                .country(league.getCountry())
                .season(league.getSeason())
                .build();
    }

    private TeamAnalysis mapTeamAnalysis(Prediction.TeamDetails team) {
        if (team == null) {
            return null;
        }

        return TeamAnalysis.builder()
                .id(team.getId())
                .name(team.getName())
                .seasonStats(mapSeasonStats(team.getLeague()))
                .recentForm(mapRecentForm(team.getLast5()))
                .build();
    }

    private SeasonStats mapSeasonStats(Prediction.LeagueStats leagueStats) {
        if (leagueStats == null) {
            return null;
        }

        return SeasonStats.builder()
                .formString(leagueStats.getForm())
                .fixtures(mapFixtureRecord(leagueStats.getFixtures()))
                .goals(mapGoalRecord(leagueStats.getGoals()))
                .cleanSheets(mapVenueRecord(leagueStats.getCleanSheet()))
                .failedToScore(mapVenueRecord(leagueStats.getFailedToScore()))
                .biggestStreak(mapStreakRecord(leagueStats.getBiggest()))
                .build();
    }

    private FixtureRecord mapFixtureRecord(Prediction.FixtureStats fixtures) {
        if (fixtures == null) {
            return null;
        }

        return FixtureRecord.builder()
                .played(mapVenueRecord(fixtures.getPlayed()))
                .wins(mapVenueRecord(fixtures.getWins()))
                .draws(mapVenueRecord(fixtures.getDraws()))
                .losses(mapVenueRecord(fixtures.getLoses()))
                .build();
    }

    private VenueRecord mapVenueRecord(Prediction.HomeAwayTotal homeAwayTotal) {
        if (homeAwayTotal == null) {
            return null;
        }

        return VenueRecord.builder()
                .home(homeAwayTotal.getHome())
                .away(homeAwayTotal.getAway())
                .total(homeAwayTotal.getTotal())
                .build();
    }

    private GoalRecord mapGoalRecord(Prediction.LeagueGoals goals) {
        if (goals == null) {
            return null;
        }

        return GoalRecord.builder()
                .scored(mapGoalDetails(goals.getForGoals()))
                .conceded(mapGoalDetails(goals.getAgainst()))
                .build();
    }

    private GoalDetails mapGoalDetails(Prediction.DetailedGoals detailedGoals) {
        if (detailedGoals == null) {
            return null;
        }

        return GoalDetails.builder()
                .total(mapVenueRecord(detailedGoals.getTotal()))
                .average(mapVenueAverage(detailedGoals.getAverage()))
                .build();
    }

    private VenueAverage mapVenueAverage(Prediction.HomeAwayAverage homeAwayAverage) {
        if (homeAwayAverage == null) {
            return null;
        }

        return VenueAverage.builder()
                .home(homeAwayAverage.getHome())
                .away(homeAwayAverage.getAway())
                .total(homeAwayAverage.getTotal())
                .build();
    }

    private StreakRecord mapStreakRecord(Prediction.Biggest biggest) {
        if (biggest == null || biggest.getStreak() == null) {
            return null;
        }

        Prediction.Streak streak = biggest.getStreak();
        return StreakRecord.builder()
                .wins(streak.getWins())
                .draws(streak.getDraws())
                .losses(streak.getLoses())
                .build();
    }

    private RecentFormStats mapRecentForm(Prediction.Last5 last5) {
        if (last5 == null) {
            return null;
        }

        return RecentFormStats.builder()
                .matchesPlayed(last5.getPlayed())
                .goals(mapSimpleGoalStats(last5.getGoals()))
                .build();
    }

    private SimpleGoalStats mapSimpleGoalStats(Prediction.Last5Goals goals) {
        if (goals == null) {
            return null;
        }

        Integer scoredTotal = goals.getForGoals() != null ? goals.getForGoals().getTotal() : null;
        String scoredAvg = goals.getForGoals() != null ? goals.getForGoals().getAverage() : null;
        Integer concededTotal = goals.getAgainst() != null ? goals.getAgainst().getTotal() : null;
        String concededAvg = goals.getAgainst() != null ? goals.getAgainst().getAverage() : null;

        return SimpleGoalStats.builder()
                .scored(scoredTotal)
                .scoredAverage(scoredAvg)
                .conceded(concededTotal)
                .concededAverage(concededAvg)
                .build();
    }

    private List<HistoricalMatch> mapHeadToHead(List<Fixture> h2hFixtures) {
        if (h2hFixtures == null || h2hFixtures.isEmpty()) {
            return Collections.emptyList();
        }

        return h2hFixtures.stream()
                .map(this::mapHistoricalMatch)
                .collect(Collectors.toList());
    }

    private HistoricalMatch mapHistoricalMatch(Fixture fixture) {
        if (fixture == null) {
            return null;
        }

        return HistoricalMatch.builder()
                .date(fixture.getFixture() != null ? fixture.getFixture().getDate() : null)
                .season(fixture.getLeague() != null ? String.valueOf(fixture.getLeague().getSeason()) : null)
                .round(fixture.getLeague() != null ? fixture.getLeague().getRound() : null)
                .venue(fixture.getFixture() != null && fixture.getFixture().getVenue() != null ? 
                        fixture.getFixture().getVenue().getName() : null)
                .homeTeam(mapTeamResult(
                        fixture.getTeams() != null ? fixture.getTeams().getHome() : null,
                        fixture.getGoals() != null ? fixture.getGoals().getHome() : null))
                .awayTeam(mapTeamResult(
                        fixture.getTeams() != null ? fixture.getTeams().getAway() : null,
                        fixture.getGoals() != null ? fixture.getGoals().getAway() : null))
                .build();
    }

    private TeamResult mapTeamResult(Fixture.Team team, Integer goals) {
        if (team == null) {
            return null;
        }

        return TeamResult.builder()
                .id(team.getId())
                .name(team.getName())
                .goals(goals)
                .build();
    }
}
