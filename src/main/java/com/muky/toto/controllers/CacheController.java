package com.muky.toto.controllers;

import com.muky.toto.ai_response.TodoPredictionPromptResponse;
import com.muky.toto.cache.CompressionUtils;
import com.muky.toto.cache.RedisCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
public class CacheController implements CacheApi {
    private final RedisCacheManager redisCacheManager;

    public CacheController(RedisCacheManager redisCacheManager) {
        this.redisCacheManager = redisCacheManager;
    }

    @Override
    public ResponseEntity<String> get(String key) throws IOException {
        Optional<byte[]> bytes = redisCacheManager.get(key);
        return ResponseEntity.ok(bytes.isPresent() ? CompressionUtils.decompress(bytes.get()) : "");
    }

    @Override
    public ResponseEntity<String> put(String key, String value) throws IOException {
        redisCacheManager.set(key, 60, value);
        return ResponseEntity.ok("Save for 1 minute");
    }

    @Override
    public ResponseEntity<String> delete(String key) {
        Long deletedCount = redisCacheManager.delete(key);
        return ResponseEntity.ok("Deleted " + deletedCount + " key(s)");
    }

    @Override
    public ResponseEntity<String> deleteAll() {
        String result = redisCacheManager.deleteAll();
        return ResponseEntity.ok("Cache cleared: " + result);
    }

    @Override
    public ResponseEntity<Optional<TodoPredictionPromptResponse>> getPrediction(String team1, String team2, String lang) {
        log.info("Getting prediction for {} vs {} in {}", team1, team2, lang);
        Optional<TodoPredictionPromptResponse> prediction = redisCacheManager.getPrediction(team1, team2, lang);
        log.info("Prediction: {}", prediction);
        return ResponseEntity.ok(prediction);
    }


}
