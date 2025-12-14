package com.muky.toto.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ObjectMapperService {
    private final ObjectMapper objectMapper;

    public ObjectMapperService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String compress(String value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            log.error("Failed to compress data", e);
            return null;
        }
    }

    public String decompress(String value) {
        try {
            return objectMapper.readValue(value, String.class);
        } catch (Exception e) {
            log.error("Failed to decompress data", e);
            return null;
        }
    }

    public <T> T parseJson(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (Exception e) {
            log.error("Failed to parse JSON", e);
            return null;
        }
    }


}
