package com.muky.toto.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/loggers")
@Tag(name = "Logger Management", description = "Endpoints for managing application logger levels")
public class LoggerController {

    private final LoggingSystem loggingSystem;

    public LoggerController(LoggingSystem loggingSystem) {
        this.loggingSystem = loggingSystem;
    }

    @GetMapping("/{loggerName}")
    @Operation(summary = "Get logger level", description = "Retrieves the current log level for a specific logger")
    public ResponseEntity<Map<String, String>> getLoggerLevel(
            @Parameter(description = "Logger name (e.g., com.muky.toto)", required = true)
            @PathVariable String loggerName) {
        
        LogLevel logLevel = loggingSystem.getLoggerConfiguration(loggerName).getEffectiveLevel();
        Map<String, String> response = new HashMap<>();
        response.put("logger", loggerName);
        response.put("level", logLevel != null ? logLevel.name() : "NOT_SET");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{loggerName}")
    @Operation(summary = "Set logger level", description = "Sets the log level for a specific logger")
    public ResponseEntity<Map<String, String>> setLoggerLevel(
            @Parameter(description = "Logger name (e.g., com.muky.toto)", required = true)
            @PathVariable String loggerName,
            @Parameter(description = "Log level (TRACE, DEBUG, INFO, WARN, ERROR, OFF)", required = true)
            @RequestParam String level) {
        
        try {
            LogLevel logLevel = LogLevel.valueOf(level.toUpperCase());
            loggingSystem.setLogLevel(loggerName, logLevel);
            
            Map<String, String> response = new HashMap<>();
            response.put("logger", loggerName);
            response.put("level", logLevel.name());
            response.put("status", "updated");
            
            log.info("Logger level changed: {} -> {}", loggerName, logLevel);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid log level: " + level);
            error.put("validLevels", "TRACE, DEBUG, INFO, WARN, ERROR, OFF");
            return ResponseEntity.badRequest().body(error);
        }
    }
}
