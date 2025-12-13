package com.muky.toto.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.muky.toto.client.api_football.ApiFootballAdapter;
import com.muky.toto.cache.CompressionUtils;
import com.muky.toto.cache.RedisCacheManager;
import com.muky.toto.client.api_football.ApiFootballClient;
import com.muky.toto.client.api_football.League;
import com.muky.toto.client.api_football.Standing;
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
                redisCacheManager.set(cacheKey, 60*60*24, jsonNode.toString());
            } catch (IOException e) {
                log.error("Failed to compress data of getIsraelLeagues", e);
                throw new RuntimeException(e);
            }
            leagues = apiFootballAdapter.parseLeagues(jsonNode);
        }
        return leagues;
    }

    public List<Standing> getIsraelPremierLeagueStandings(int season) {
        int israelPremierLeagueId = 383;
        JsonNode jsonNode = apiFootballClient.getStandings(israelPremierLeagueId, season);
        return apiFootballAdapter.parseStandings(jsonNode);
    }

    public byte[] getIsraelLeaguesAsBytes() {
        JsonNode jsonNode = apiFootballClient.getLeagues("Israel");
        return apiFootballAdapter.toBytes(jsonNode);
    }

    public byte[] getIsraelPremierLeagueStandingsAsBytes(int season) {
        int israelPremierLeagueId = 383;
        JsonNode jsonNode = apiFootballClient.getStandings(israelPremierLeagueId, season);
        return apiFootballAdapter.toBytes(jsonNode);
    }

    public List<League> parseLeaguesFromBytes(byte[] bytes) {
        JsonNode jsonNode = apiFootballAdapter.fromBytes(bytes);
        return apiFootballAdapter.parseLeagues(jsonNode);
    }

    public List<Standing> parseStandingsFromBytes(byte[] bytes) {
        JsonNode jsonNode = apiFootballAdapter.fromBytes(bytes);
        return apiFootballAdapter.parseStandings(jsonNode);
    }
}
