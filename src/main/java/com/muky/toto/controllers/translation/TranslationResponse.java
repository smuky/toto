package com.muky.toto.controllers.translation;

import com.muky.toto.model.LeagueEnum;
import com.muky.toto.model.PredefinedEvent;

import java.util.List;
import java.util.Map;

public record TranslationResponse(
        Map<LeagueEnum, LeagueTranslation> leagueTranslations,
        Map<String, String> languageTranslations,
        List<PredefinedEvent> predefinedEvents,
        UpgradeMessages upgradeMessages,
        PremiumBadgeMessages premiumBadgeMessages,
        Days days,
        SettingsTranslation settingsTranslation,
        SendFeedbackTranslation sendFeedbackTranslation,
        String selectLeagueMode,
        String recommendedListsMode,
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
        String analyzeMatch,
        String termsOfUseTitle,
        String termsOfUseHeader,
        String termsOfUseStatisticalInfo,
        String termsOfUseNotGambling,
        String termsOfUseAgeRequirement,
        String termsOfUseNoResponsibility,
        String termsOfUseReadPolicy,
        String termsOfUseAgreeContinue
) {
}
