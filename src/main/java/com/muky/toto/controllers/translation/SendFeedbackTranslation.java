package com.muky.toto.controllers.translation;

public record SendFeedbackTranslation(
        String title,
        String description,
        String messageLabel,
        String messagePlaceholder,
        String emailLabel,
        String emailPlaceholder,
        String sendButton
) {
}
