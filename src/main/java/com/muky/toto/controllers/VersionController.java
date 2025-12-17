package com.muky.toto.controllers;

import com.muky.toto.controllers.response.VersionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/version")
@Tag(name = "Version", description = "App version APIs")
public class VersionController {

    @Value("${app.min-version:1.0.0+25}")
    private String appMinVersion;

    @Operation(
            summary = "Get minimum app version",
            description = "Returns the minimum required app version for the client"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved minimum version")
    })
    @GetMapping
    public ResponseEntity<VersionResponse> getMinVersion() {
        log.info("Version check requested. Returning minimum version: {}", appMinVersion);
        VersionResponse response = new VersionResponse(appMinVersion);
        return ResponseEntity.ok(response);
    }
}
