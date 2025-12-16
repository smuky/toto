package com.muky.toto.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.muky.toto.client.api_football.ApiFootballAdapter;
import com.muky.toto.cache.CompressionUtils;
import com.muky.toto.cache.RedisCacheManager;
import com.muky.toto.client.api_football.ApiFootballClient;
import com.muky.toto.client.api_football.Fixture;
import com.muky.toto.client.api_football.League;
import com.muky.toto.client.api_football.Standing;
import com.muky.toto.model.LeagueEnum;
import com.muky.toto.model.apifootball.SupportedCountriesEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiFootballService {

    public static final int DAYS_IN_SECONDS = 60 * 60 * 24;
    private final ApiFootballClient apiFootballClient;
    private final ApiFootballAdapter apiFootballAdapter;
    private final RedisCacheManager redisCacheManager;
    private final ObjectMapperService objectMapperService;

    public List<League> getLeagues(SupportedCountriesEnum country) {
        List<League> leagues = null;
        String cacheKey = "leagues." + country.getName();
        Optional<byte[]> bytes = redisCacheManager.get(cacheKey);
        if (bytes.isPresent()) {
            log.info("Using cached value for leagues.Israel");
            try {
                String json = CompressionUtils.decompress(bytes.get());
                JsonNode jsonNode = objectMapperService.parseJson(json, JsonNode.class);
                leagues = apiFootballAdapter.parseLeagues(jsonNode);
            } catch (IOException e) {
                log.error("Failed to extract json from bytes for getIsraelLeagues", e);
                throw new RuntimeException(e);
            }
        } else {
            JsonNode jsonNode = apiFootballClient.getLeagues(country.getName());
            log.info(country.getName() + "leagues: {}", jsonNode);

            try {
                redisCacheManager.set(cacheKey, DAYS_IN_SECONDS, jsonNode.toString());
            } catch (IOException e) {
                log.error("Failed to compress data of getIsraelLeagues", e);
                throw new RuntimeException(e);
            }
            leagues = apiFootballAdapter.parseLeagues(jsonNode);
        }
        return leagues;
    }

    public List<Standing.StandingEntry> getStandingEntries(int teamId1, int teamId2, LeagueEnum leagueEnum) {
        Standing standing = getStanding(leagueEnum);
        List<List<Standing.StandingEntry>> standings = standing.getLeague().getStandings();
        return standings.stream()
                .flatMap(List::stream)
                .filter(entry -> entry.getTeam().getId() == teamId1 || entry.getTeam().getId() == teamId2)
                .toList();
    }

    public Standing getStanding(LeagueEnum leagueEnum) {
        Standing standings = null;
        String cacheKey = "standings." + leagueEnum.name();
        Optional<byte[]> bytes = redisCacheManager.get(cacheKey);
        if (bytes.isPresent()) {
            log.info("Using cached value for standings.{}", leagueEnum.name());
            try {
                String json = CompressionUtils.decompress(bytes.get());
                JsonNode jsonNode = objectMapperService.parseJson(json, JsonNode.class);
                standings = apiFootballAdapter.parseStandingsResponse(jsonNode);
            } catch (IOException e) {
                log.error("Failed to extract json from bytes for getStandings", e);
                throw new RuntimeException(e);
            }
        } else {
            JsonNode jsonNode = apiFootballClient.getStandings(leagueEnum);
            log.info("{} standings: {}", leagueEnum.name(), jsonNode);

            try {
                redisCacheManager.set(cacheKey, DAYS_IN_SECONDS, jsonNode.toString());
            } catch (IOException e) {
                log.error("Failed to compress data of getStandings", e);
                throw new RuntimeException(e);
            }
            standings = apiFootballAdapter.parseStandingsResponse(jsonNode);
        }
        return standings;
    }

    public byte[] getIsraelLeaguesAsBytes() {
        JsonNode jsonNode = apiFootballClient.getLeagues("Israel");
        return apiFootballAdapter.toBytes(jsonNode);
    }

    public List<League> parseLeaguesFromBytes(byte[] bytes) {
        JsonNode jsonNode = apiFootballAdapter.fromBytes(bytes);
        return apiFootballAdapter.parseLeagues(jsonNode);
    }

    public List<Fixture> getNextFixtures(LeagueEnum leagueEnum, int next) {
        List<Fixture> fixtures = null;
        String cacheKey = "fixtures." + leagueEnum.name() + ".next." + next;
        Optional<byte[]> bytes = redisCacheManager.get(cacheKey);
        if (bytes.isPresent()) {
            log.info("Using cached value for fixtures.{}.next.{}", leagueEnum.name(), next);
            try {
                String json = CompressionUtils.decompress(bytes.get());
                JsonNode jsonNode = objectMapperService.parseJson(json, JsonNode.class);
                fixtures = apiFootballAdapter.parseFixtures(jsonNode);
            } catch (IOException e) {
                log.error("Failed to extract json from bytes for getNextFixtures", e);
                throw new RuntimeException(e);
            }
        } else {
            JsonNode jsonNode = apiFootballClient.getNextFixtures(leagueEnum, next);
            log.info("{} next {} fixtures: {}", leagueEnum.name(), next, jsonNode);

            try {
                redisCacheManager.set(cacheKey, 60 * 60, jsonNode.toString()); // Cache for 1 hour
            } catch (IOException e) {
                log.error("Failed to compress data of getNextFixtures", e);
                throw new RuntimeException(e);
            }
            fixtures = apiFootballAdapter.parseFixtures(jsonNode);
        }
        return fixtures;
    }

    public JsonNode getPredictions(int fixtureId) {
        JsonNode predictions = null;
        String cacheKey = "predictions.fixture." + fixtureId;
        Optional<byte[]> bytes = redisCacheManager.get(cacheKey);
        if (bytes.isPresent()) {
            log.info("Using cached value for predictions.fixture.{}", fixtureId);
            try {
                String json = CompressionUtils.decompress(bytes.get());
                predictions = objectMapperService.parseJson(json, JsonNode.class);
            } catch (IOException e) {
                log.error("Failed to extract json from bytes for getPredictions", e);
                throw new RuntimeException(e);
            }
        } else {
            predictions = apiFootballClient.getPredictions(fixtureId);
            log.info("Predictions for fixture {}: {}", fixtureId, predictions);

            try {
                redisCacheManager.set(cacheKey, DAYS_IN_SECONDS, predictions.toString()); // Cache for 24 hours
            } catch (IOException e) {
                log.error("Failed to compress data of getPredictions", e);
                throw new RuntimeException(e);
            }
        }
        return predictions;
    }
}
