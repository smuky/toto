package com.muky.toto.cache;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muky.toto.ai_response.ApiFootballPredictionResponse;
import com.muky.toto.ai_response.TodoPredictionPromptResponse;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
public class RedisCacheManager {
    private final UnifiedJedis jedis;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final int redisExpireSeconds;

    public RedisCacheManager(
            @Value("${redis.host}") String redisHost,
            @Value("${redis.port}") int redisPort,
            @Value("${redis.password}") String redisPassword,
            @Value("${redis.expire-seconds}") int redisExpireSeconds
    ) {
        this.redisExpireSeconds = redisExpireSeconds;

        log.info("Redis connection going to initialize to {}:{}, {}", redisHost, redisPort, redisPassword);

        JedisClientConfig config = DefaultJedisClientConfig.builder()
                .user("default")
                .password(redisPassword)
                .build();

        log.info("past config");

        jedis = new UnifiedJedis(
                new HostAndPort(redisHost, redisPort),
                config
        );
        
        log.info("Redis connection initialized to {}:{}", redisHost, redisPort);
    }

    private String generateKey(String team1, String team2, String lang) {
        String[] teams = {team1, team2};
        Arrays.sort(teams); // Sorts "Lakers", "Celtics" -> "Celtics", "Lakers"
        return String.format("game:%s.%s.%s", teams[0], teams[1], lang);

    }

    // TRICK #2: Set with Expiration (SETEX)
    public void cachePrediction(String team1, String team2, String lang, TodoPredictionPromptResponse predictionResponse) {
        String key = generateKey(team1, team2, lang);

        try {
            String json = objectMapper.writeValueAsString(predictionResponse);
            byte[] compressedData = CompressionUtils.compress(json);
            jedis.setex(key.getBytes(), redisExpireSeconds, compressedData);
            log.info("Stored key {}", key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Optional<TodoPredictionPromptResponse> getPrediction(String team1, String team2, String lang) {
        log.info("Getting prediction for {} vs {} in {}", team1, team2, lang);
        String key = generateKey(team1, team2, lang);
        Optional<byte[]> bytes = get(key);

        if (!bytes.isPresent()) {
            return Optional.empty();
        }

        try {
            String json = CompressionUtils.decompress(bytes.get());
            TodoPredictionPromptResponse response = objectMapper.readValue(json, TodoPredictionPromptResponse.class);
        return Optional.of(response);
        } catch (IOException e) {
            log.error("Redis Cache fail on " + team1 + "." + team2 + "." + lang + "due to: ", e);
            return Optional.empty();
        }
    }

    private String generateFixtureKey(int fixtureId, String lang) {
        return String.format("fixture:%d.%s", fixtureId, lang);
    }

    public void cacheApiFootballPrediction(int fixtureId, String lang, TodoPredictionPromptResponse predictionResponse) {
        String key = generateFixtureKey(fixtureId, lang);

        try {
            String json = objectMapper.writeValueAsString(predictionResponse);
            byte[] compressedData = CompressionUtils.compress(json);
            jedis.setex(key.getBytes(), redisExpireSeconds, compressedData);
            log.info("Stored API-Football prediction key {}", key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<TodoPredictionPromptResponse> getApiFootballPrediction(int fixtureId, String lang) {
        log.info("Getting API-Football prediction for fixture {} in {}", fixtureId, lang);
        String key = generateFixtureKey(fixtureId, lang);
        Optional<byte[]> bytes = get(key);

        if (!bytes.isPresent()) {
            return Optional.empty();
        }

        try {
            String json = CompressionUtils.decompress(bytes.get());
            TodoPredictionPromptResponse response = objectMapper.readValue(json, TodoPredictionPromptResponse.class);
            return Optional.of(response);
        } catch (IOException e) {
            log.error("Redis Cache fail on fixture {} in {} due to: ", fixtureId, lang, e);
            return Optional.empty();
        }
    }
    public String set(String key, int seconds, String value) throws IOException {
        byte[] compress = CompressionUtils.compress(value);
        return jedis.setex(key.getBytes(), seconds, compress);
    }

    public Optional<byte[]> get(String key) {
        byte[] data = jedis.get(key.getBytes());
        return Optional.ofNullable(data);
    }

    public Long delete(String key) {
        return jedis.del(key.getBytes());
    }

    public String deleteAll() {
        return jedis.flushDB();
    }

    @PreDestroy
    public void cleanup() {
        if (jedis != null) {
            jedis.close();
            log.info("Redis connection closed successfully");
        }
    }
}
