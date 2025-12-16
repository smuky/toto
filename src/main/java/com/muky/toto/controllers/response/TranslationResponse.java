package com.muky.toto.controllers.response;

import com.muky.toto.model.LeagueEnum;

import java.util.Map;

public record TranslationResponse(
        Map<LeagueEnum, String> leagueTranslations,
        Map<String, String> languageTranslations,
        String selectLeague,
        String settings,
        String about,
        String draw,
        String vs,
        String winProbabilities,
        String predictionJustification,
        String detailedAnalysis,
        String recentFormAnalysis,
        String expectedGoalsAnalysis,
        String headToHeadSummary,
        String keyNewsInjuries,
        String results,
        String customMatch,
        String upcomingGames,
        String analyzing,
        String analyzeMatch
) {
}
