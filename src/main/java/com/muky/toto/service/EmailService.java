package com.muky.toto.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${feedback.recipient.email}")
    private String recipientEmail;

    public void sendFeedback(String message, String userEmail, String appVersion, String buildNumber, 
                              String deviceModel, String operatingSystem, String locale, String timezone) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName(); // This is the UID you set in the authentication object

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(recipientEmail);
            mailMessage.setSubject("AI Football Predictor - User Feedback");
            
            StringBuilder emailBody = new StringBuilder();
            emailBody.append("New feedback received:\n\n");
            emailBody.append("=== MESSAGE ===\n");
            emailBody.append(message);
            emailBody.append("\n\n");
            
            emailBody.append("=== USER INFO ===\n");
            if (userEmail != null && !userEmail.isBlank()) {
                emailBody.append("Email: ").append(userEmail).append("\n");
                mailMessage.setReplyTo(userEmail);
            } else {
                emailBody.append("Email: Not provided\n");
            }
            emailBody.append("User ID: ").append(userId).append("\n");
            
            emailBody.append("\n=== APP INFO ===\n");
            emailBody.append("Version: ").append(appVersion != null ? appVersion : "N/A").append("\n");
            emailBody.append("Build: ").append(buildNumber != null ? buildNumber : "N/A").append("\n");
            
            emailBody.append("\n=== DEVICE INFO ===\n");
            emailBody.append("Model: ").append(deviceModel != null ? deviceModel : "N/A").append("\n");
            emailBody.append("OS: ").append(operatingSystem != null ? operatingSystem : "N/A").append("\n");
            emailBody.append("Locale: ").append(locale != null ? locale : "N/A").append("\n");
            emailBody.append("Timezone: ").append(timezone != null ? timezone : "N/A").append("\n");
            
            mailMessage.setText(emailBody.toString());
            
            mailSender.send(mailMessage);
            log.info("Feedback email sent successfully to {}", recipientEmail);
            
        } catch (Exception e) {
            log.error("Failed to send feedback email", e);
            throw new RuntimeException("Failed to send feedback email: " + e.getMessage(), e);
        }
    }
}
