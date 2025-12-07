package com.muky.toto.controllers.response;

import com.muky.toto.model.TeamScoreEntry;

import java.util.List;

public record AllTeamsResponse(
        List<TeamScoreEntry> teams,
        TranslationResponse translations
) {
}
