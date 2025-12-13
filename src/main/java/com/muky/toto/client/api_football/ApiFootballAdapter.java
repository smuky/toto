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

    public ApiFootballResponse<Standing> parseStandingsResponse(JsonNode jsonNode) {
        try {
            return objectMapper.convertValue(
                    jsonNode,
                    new TypeReference<ApiFootballResponse<Standing>>() {}
            );
        } catch (Exception e) {
            log.error("Failed to parse standings response", e);
            throw new RuntimeException("Failed to parse standings response", e);
        }
    }

    public List<Standing> parseStandings(JsonNode jsonNode) {
        ApiFootballResponse<Standing> response = parseStandingsResponse(jsonNode);
        return response.getResponse();
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
}
