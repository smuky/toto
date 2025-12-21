package com.muky.toto.controllers.response;

public record UpgradeMessages(
        String header,
        String body,
        String currentVersion,
        String requiredVersion,
        String button
) {
}
