package com.muky.toto.controllers.translation;

public record UpgradeMessages(
        String header,
        String body,
        String currentVersion,
        String requiredVersion,
        String button
) {
}
