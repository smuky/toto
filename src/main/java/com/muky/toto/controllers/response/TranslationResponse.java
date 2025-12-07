package com.muky.toto.controllers.response;

import com.muky.toto.model.LeagueEnum;

import java.util.Map;

public record TranslationResponse(
        Map<LeagueEnum, String> leagueTranslations,
        Map<String, String> languageTranslations,
        String selectLeague,
        String settings,
        String about
) {
}
