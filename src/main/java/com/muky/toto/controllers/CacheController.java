package com.muky.toto.controllers;

import com.muky.toto.cache.RedisCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CacheController implements CacheApi {
    private final RedisCacheManager redisCacheManager;

    public CacheController(RedisCacheManager redisCacheManager) {
        this.redisCacheManager = redisCacheManager;
    }

    @Override
    public ResponseEntity<String> get(String key) {
        String s = redisCacheManager.get(key);
        return ResponseEntity.ok(s);
    }

    @Override
    public ResponseEntity<String> put(String key, String value) {
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


}
