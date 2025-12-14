package com.muky.toto.client.api_football;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiFootballAdapter {

    private final ObjectMapper objectMapper;

    public ApiFootballResponse<League> parseLeaguesResponse(JsonNode jsonNode) {
        try {
            return objectMapper.convertValue(
                    jsonNode,
                    new TypeReference<ApiFootballResponse<League>>() {}
            );
        } catch (Exception e) {
            log.error("Failed to parse leagues response", e);
            throw new RuntimeException("Failed to parse leagues response", e);
        }
    }

    public List<League> parseLeagues(JsonNode jsonNode) {
        ApiFootballResponse<League> response = parseLeaguesResponse(jsonNode);
        return response.getResponse();
    }

    public Standing parseStandingsResponse(JsonNode jsonNode) {
        try {
            ApiFootballResponse<Standing> apiResponse = objectMapper.convertValue(
                    jsonNode,
                    new TypeReference<ApiFootballResponse<Standing>>() {}
            );
            List<Standing> standings = apiResponse.getResponse();
            if (standings == null || standings.isEmpty()) {
                throw new RuntimeException("No standings found in response");
            }
            Standing standing = standings.get(0);

            List<List<Standing.StandingEntry>> standings1 = standing.getLeague().getStandings();
            while (standings1.size() > 1) {
                standings1.removeFirst();
            }
            return standing;
        } catch (Exception e) {
            log.error("Failed to parse standings response", e);
            throw new RuntimeException("Failed to parse standings response", e);
        }
    }

    public byte[] toBytes(JsonNode jsonNode) {
        try {
            return objectMapper.writeValueAsBytes(jsonNode);
        } catch (Exception e) {
            log.error("Failed to convert JsonNode to bytes", e);
            throw new RuntimeException("Failed to convert JsonNode to bytes", e);
        }
    }

    public JsonNode fromBytes(byte[] bytes) {
        try {
            return objectMapper.readTree(bytes);
        } catch (Exception e) {
            log.error("Failed to convert bytes to JsonNode", e);
            throw new RuntimeException("Failed to convert bytes to JsonNode", e);
        }
    }

    public List<Fixture> parseFixtures(JsonNode jsonNode) {
        try {
            ApiFootballResponse<Fixture> apiResponse = objectMapper.convertValue(
                    jsonNode,
                    new TypeReference<ApiFootballResponse<Fixture>>() {}
            );
            List<Fixture> fixtures = apiResponse.getResponse();
            if (fixtures == null) {
                throw new RuntimeException("No fixtures found in response");
            }
            return fixtures;
        } catch (Exception e) {
            log.error("Failed to parse fixtures response", e);
            throw new RuntimeException("Failed to parse fixtures response", e);
        }
    }
}
