package com.muky.toto.service;

import com.muky.toto.controllers.translation.Days;
import com.muky.toto.controllers.translation.LeagueTranslation;
import com.muky.toto.controllers.translation.PremiumBadgeMessages;
import com.muky.toto.controllers.translation.TranslationResponse;
import com.muky.toto.controllers.translation.UpgradeMessages;
import com.muky.toto.model.LeagueEnum;
import com.muky.toto.model.PredefinedEvent;
import com.muky.toto.model.TeamTranslationMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslationService {

    private final MessageSource messageSource;
    private final Environment environment;

    public String translate(String key, String language) {
        Locale locale = Locale.forLanguageTag(language);
        return messageSource.getMessage(key, null, locale);
    }

    public String getLeagueName(LeagueEnum league, String language) {
        String translatedName = translate(league.getTranslationKey(), language);
        return translatedName;
    }
    
    public String getCountryName(String country, String language) {
        String translationKey = "country." + country.toLowerCase();
        try {
            return translate(translationKey, language);
        } catch (Exception e) {
            log.warn("Translation not found for country key: {} in language: {}, using original name", translationKey, language);
            return country;
        }
    }
    
    /**
     * Get translated team name by team ID
     * @param teamId API-Football team ID
     * @param originalName Original team name from API-Football (fallback)
     * @param language Language code (e.g., "en", "he")
     * @return Translated team name or original name if translation not found
     */
    public String getTeamName(int teamId, String originalName, String language) {
        String translationKey = TeamTranslationMapping.getTranslationKey(teamId);
        
        if (translationKey != null) {
            try {
                return translate(translationKey, language);
            } catch (Exception e) {
                log.warn("Translation not found for key: {} in language: {}, using original name", translationKey, language);
                return originalName;
            }
        }
        
        // No translation mapping exists, return original name
        return originalName;
    }

    public TranslationResponse getTranslations(String language) {
        Map<LeagueEnum, LeagueTranslation> leagueTranslations = Arrays.stream(LeagueEnum.values())
                .collect(Collectors.toMap(
                        league -> league,
                        league -> new LeagueTranslation(
                                getLeagueName(league, language),
                                getCountryName(league.getCountry(), language)
                        )
                ));

        Map<String, String> languageTranslations = Map.of(
                "en", translate("language.english", language),
                "es", translate("language.spanish", language),
                "it", translate("language.italian", language),
                "de", translate("language.german", language),
                "fr", translate("language.french", language),
                "he", translate("language.hebrew", language)
        );

        UpgradeMessages upgradeMessages = new UpgradeMessages(
                translate("upgrade.minimum.version.header", language),
                translate("upgrade.minimum.version.body", language),
                translate("upgrade.minimum.version.current_version", language),
                translate("upgrade.minimum.version.required_version", language),
                translate("upgrade.minimum.version.button", language)
        );
        
        PremiumBadgeMessages premiumBadgeMessages = new PremiumBadgeMessages(
                translate("recommended.lists.premium_badge.title", language),
                translate("recommended.lists.premium_badge.body", language),
                translate("recommended.lists.premium_badge.button", language),
                translate("recommended.lists.premium_badge.back", language)
        );

        Days days = new Days(
                translate("day.monday", language),
                translate("day.tuesday", language),
                translate("day.wednesday", language),
                translate("day.thursday", language),
                translate("day.friday", language),
                translate("day.saturday", language),
                translate("day.sunday", language)
        );

        String selectLeagueMode = translate("select.league.mode", language);
        String recommendedListsMode = translate("recommended.lists.mode", language);
        String selectLeague = translate("select.league", language);
        String settings = translate("settings", language);
        String about = translate("about", language);
        String draw = translate("draw", language);
        String vs = translate("vs", language);
        String winProbabilities = translate("winProbabilities", language);
        String predictionJustification = translate("predictionJustification", language);
        String detailedAnalysis = translate("detailedAnalysis", language);
        String recentFormAnalysis = translate("recentFormAnalysis", language);
        String expectedGoalsAnalysis = translate("expectedGoalsAnalysis", language);
        String headToHeadSummary = translate("headToHeadSummary", language);
        String keyNewsInjuries = translate("keyNewsInjuries", language);
        String results = translate("results", language);
        String customMatch = translate("customMatch", language);
        String upcomingGames = translate("upcomingGames", language);
        String analyzing = translate("analyzing", language);
        String analyzeMatch = translate("analyzeMatch", language);
        String termsOfUseTitle = translate("terms_of_use.title", language);
        String termsOfUseHeader = translate("terms_of_use.header", language);
        String termsOfUseStatisticalInfo = translate("terms_of_use.statistical_info", language);
        String termsOfUseNotGambling = translate("terms_of_use.not_gambling", language);
        String termsOfUseAgeRequirement = translate("terms_of_use.age_requirement", language);
        String termsOfUseNoResponsibility = translate("terms_of_use.no_responsibility", language);
        String termsOfUseReadPolicy = translate("terms_of_use.read_policy", language);
        String termsOfUseAgreeContinue = translate("terms_of_use.agree_continue", language);

        List<PredefinedEvent> predefinedEvents = getPredefinedEvents(language);

        return new TranslationResponse(
                leagueTranslations,
                languageTranslations,
                predefinedEvents,
                upgradeMessages,
                premiumBadgeMessages,
                days,
                selectLeagueMode,
                recommendedListsMode,
                selectLeague,
                settings,
                about,
                draw,
                vs,
                winProbabilities,
                predictionJustification,
                detailedAnalysis,
                recentFormAnalysis,
                expectedGoalsAnalysis,
                headToHeadSummary,
                keyNewsInjuries,
                results,
                customMatch,
                upcomingGames,
                analyzing,
                analyzeMatch,
                termsOfUseTitle,
                termsOfUseHeader,
                termsOfUseStatisticalInfo,
                termsOfUseNotGambling,
                termsOfUseAgeRequirement,
                termsOfUseNoResponsibility,
                termsOfUseReadPolicy,
                termsOfUseAgreeContinue
        );
    }

    public List<PredefinedEvent> getPredefinedEvents(String language) {
        List<PredefinedEvent> events = new ArrayList<>();
        
        // Get all properties that start with "app.predefined-events."
        String prefix = "app.predefined-events.";
        org.springframework.core.env.MutablePropertySources propertySources = 
            ((org.springframework.core.env.AbstractEnvironment) environment).getPropertySources();
        
        propertySources.forEach(propertySource -> {
            if (propertySource instanceof org.springframework.core.env.EnumerablePropertySource) {
                org.springframework.core.env.EnumerablePropertySource<?> enumerable = 
                    (org.springframework.core.env.EnumerablePropertySource<?>) propertySource;
                for (String propertyName : enumerable.getPropertyNames()) {
                    if (propertyName.startsWith(prefix)) {
                        String eventKey = propertyName.substring(prefix.length());
                        String translationKey = "predefined-event." + eventKey;
                        String displayName;
                        try {
                            displayName = translate(translationKey, language);
                        } catch (Exception e) {
                            log.warn("Translation not found for key: {} in language: {}, using key as display name", translationKey, language);
                            displayName = eventKey;
                        }
                        events.add(new PredefinedEvent(eventKey, displayName));
                    }
                }
            }
        });
        
        log.info("Found {} predefined events for language: {}", events.size(), language);
        return events;
    }
}
