package com.muky.toto.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("league-type", "team-games");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(50)  // Limit to 50 entries per cache
                .expireAfterWrite(1, TimeUnit.HOURS)  // Expire after 1 hour
                .recordStats());
        return cacheManager;
    }
}
