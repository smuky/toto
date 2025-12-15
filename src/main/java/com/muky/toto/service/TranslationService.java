package com.muky.toto.service;

import com.muky.toto.controllers.response.TranslationResponse;
import com.muky.toto.model.LeagueEnum;
import com.muky.toto.model.TeamTranslationMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslationService {

    private final MessageSource messageSource;

    public String translate(String key, String language) {
        Locale locale = Locale.forLanguageTag(language);
        return messageSource.getMessage(key, null, locale);
    }

    public String getLeagueName(LeagueEnum league, String language) {
        return translate(league.getTranslationKey(), language);
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
        Map<LeagueEnum, String> leagueTranslations = Arrays.stream(LeagueEnum.values())
                .collect(Collectors.toMap(
                        league -> league,
                        league -> getLeagueName(league, language)
                ));

        Map<String, String> languageTranslations = Map.of(
                "en", translate("language.english", language),
                "es", translate("language.spanish", language),
                "it", translate("language.italian", language),
                "de", translate("language.german", language),
                "fr", translate("language.french", language),
                "he", translate("language.hebrew", language)
        );

        String selectLeague = translate("select.league", language);
        String settings = translate("settings", language);
        String about = translate("about", language);
        String draw = translate("draw", language);

        return new TranslationResponse(
                leagueTranslations,
                languageTranslations,
                selectLeague,
                settings,
                about,
                draw
        );
    }
}
