package com.muky.toto.controllers;

import com.muky.toto.controllers.requests.FeedbackRequest;
import com.muky.toto.controllers.translation.FeedbackResponse;
import com.muky.toto.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "User feedback APIs")
public class FeedbackController {

    private final EmailService emailService;

    @Operation(
            summary = "Submit user feedback",
            description = "Allows users to submit feedback which will be sent via email"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feedback submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid feedback data"),
            @ApiResponse(responseCode = "500", description = "Failed to send feedback")
    })
    @PostMapping
    public ResponseEntity<FeedbackResponse> submitFeedback(@Valid @RequestBody FeedbackRequest request) {
        log.info("Received feedback submission. User email: {}, App: {}/{}, Device: {} ({})", 
                request.userEmail() != null ? request.userEmail() : "Not provided",
                request.appVersion(),
                request.buildNumber(),
                request.deviceModel(),
                request.operatingSystem());
        
        try {
            emailService.sendFeedback(
                    request.message(), 
                    request.userEmail(),
                    request.appVersion(),
                    request.buildNumber(),
                    request.deviceModel(),
                    request.operatingSystem(),
                    request.locale(),
                    request.timezone()
            );
            
            FeedbackResponse response = new FeedbackResponse(
                    true,
                    "Thank you for your feedback! We'll review it shortly."
            );
            
            log.info("Feedback submitted successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error submitting feedback", e);
            
            FeedbackResponse response = new FeedbackResponse(
                    false,
                    "Failed to submit feedback. Please try again later."
            );
            
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
