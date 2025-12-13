package com.muky.toto.client.api_football;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiFootballClient {

    private static final String BASE_URL = "https://v3.football.api-sports.io";
    private static final String HEADER_API_KEY = "x-apisports-key";

    @Value("${app.api-football.api-key}")
    private String apiKey;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode getLeagues(String country) {
        try {
            String url = BASE_URL + "/leagues?country=" + country;
            log.info("Fetching leagues for country: {}", country);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header(HEADER_API_KEY, apiKey)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("Response headers: {}", response.headers().map());

            if (response.statusCode() != 200) {
                log.error("Failed to fetch leagues. Status: {}, Body: {}", response.statusCode(), response.body());
                throw new RuntimeException("API request failed with status: " + response.statusCode());
            }

            log.debug("Leagues response: {}", response.body());
            return objectMapper.readTree(response.body());

        } catch (Exception e) {
            log.error("Error fetching leagues for country: {}", country, e);
            throw new RuntimeException("Failed to fetch leagues", e);
        }
    }

    public JsonNode getStandings(int leagueId, int season) {
        try {
            String url = BASE_URL + "/standings?league=" + leagueId + "&season=" + season;
            log.info("Fetching standings for league: {}, season: {}", leagueId, season);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header(HEADER_API_KEY, apiKey)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.error("Failed to fetch standings. Status: {}, Body: {}", response.statusCode(), response.body());
                throw new RuntimeException("API request failed with status: " + response.statusCode());
            }

            log.debug("Standings response: {}", response.body());
            return objectMapper.readTree(response.body());

        } catch (Exception e) {
            log.error("Error fetching standings for league: {}, season: {}", leagueId, season, e);
            throw new RuntimeException("Failed to fetch standings", e);
        }
    }
}
