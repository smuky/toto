package com.muky.toto.controllers.translation;

public record SendFeedbackTranslation(
        String title,
        String description,
        String messagePlaceholder,
        String emailLabel,
        String emailPlaceholder,
        String sendButton
) {
}
