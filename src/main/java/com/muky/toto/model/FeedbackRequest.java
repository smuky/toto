package com.muky.toto.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FeedbackRequest(
        @NotBlank(message = "Message cannot be empty")
        @Size(max = 5000, message = "Message cannot exceed 5000 characters")
        String message,
        
        String userEmail,
        String appVersion,
        String buildNumber,
        String deviceModel,
        String operatingSystem,
        String locale,
        String timezone
) {}
