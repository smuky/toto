package com.muky.toto.controllers;

import com.muky.toto.ai_response.TodoPredictionPromptResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Optional;

@RequestMapping("/cache")
@Tag(name = "Cache", description = "Redis cache management APIs")
public interface CacheApi {

    @Operation(
            summary = "Get value from cache",
            description = "Retrieves a value from Redis cache by key"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved value from cache"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("")
    ResponseEntity<String> get(
            @Parameter(description = "Cache key", required = true)
            @RequestParam String key) throws IOException;

    @Operation(
            summary = "Set value in cache",
            description = "Stores a key-value pair in Redis cache"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully stored value in cache"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("")
    ResponseEntity<String> put(
            @Parameter(description = "Cache key", required = true)
            @RequestParam String key,
            @Parameter(description = "Cache value", required = true)
            @RequestParam String value) throws IOException;

    @Operation(
            summary = "Delete key from cache",
            description = "Deletes a specific key from Redis cache"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted key from cache"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("")
    ResponseEntity<String> delete(
            @Parameter(description = "Cache key to delete", required = true)
            @RequestParam String key);

    @Operation(
            summary = "Delete all keys from cache",
            description = "Clears all keys from the Redis cache database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully cleared all cache"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/all")
    ResponseEntity<String> deleteAll();

    @Operation(
            summary = "Get prediction from cache",
            description = "Retrieves a cached prediction for a match between two teams in a specific language"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved prediction from cache"),
            @ApiResponse(responseCode = "404", description = "Prediction not found in cache"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/prediction")
    ResponseEntity<Optional<TodoPredictionPromptResponse>> getPrediction(
            @Parameter(description = "First team name", required = true)
            @RequestParam String team1,
            @Parameter(description = "Second team name", required = true)
            @RequestParam String team2,
            @Parameter(description = "Language code", required = true)
            @RequestParam String lang);
}
