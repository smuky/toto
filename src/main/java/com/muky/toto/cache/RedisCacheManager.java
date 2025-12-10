package com.muky.toto.cache;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muky.toto.ai_response.TodoPredictionPromptResponse;
import com.muky.toto.config.RedisProperties;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
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
    private final RedisProperties redisProperties;

    public RedisCacheManager(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
        
        JedisClientConfig config = DefaultJedisClientConfig.builder()
                .user("default")
                .password(redisProperties.getPassword())
                .build();

        jedis = new UnifiedJedis(
                new HostAndPort(redisProperties.getHost(), redisProperties.getPort()),
                config
        );
    }

    private String generateKey(String team1, String team2, String lang) {
        String[] teams = {team1, team2};
        Arrays.sort(teams); // Sorts "Lakers", "Celtics" -> "Celtics", "Lakers"
        return String.format("game:%s.%s.%s", teams[0], teams[1], lang);

    }

    public void cachePrediction(String team1, String team2, String lang, TodoPredictionPromptResponse predictionResponse) {
        String key = generateKey(team1, team2, lang);

        try {
            String json = objectMapper.writeValueAsString(predictionResponse);
            byte[] compressedData = CompressionUtils.compress(json);
            jedis.setex(key.getBytes(), redisProperties.getExpireSeconds(), compressedData);
            log.info("Stored key {}", key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Optional<TodoPredictionPromptResponse> getPrediction(String team1, String team2, String lang) {
        String key = generateKey(team1, team2, lang);
        byte[] data = jedis.get(key.getBytes());
        
        if (data == null) {
            return Optional.empty();
        }

        try {
            String json = CompressionUtils.decompress(data);
            TodoPredictionPromptResponse response = objectMapper.readValue(json, TodoPredictionPromptResponse.class);
        return Optional.of(response);
        } catch (IOException e) {
            log.error("Redis Cache fail on " + team1 + "." + team2 + "." + lang + "due to: ", e);
            return Optional.empty();
        }
    }
    public String set(String key, int seconds, String value) {
        return jedis.setex(key, seconds, value);
    }

    public String get(String key) {

        String res2 = jedis.get(key);
        return res2;
    }

    public Long delete(String key) {
        return jedis.del(key);
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
