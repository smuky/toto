package com.muky.toto.service;

import com.muky.toto.controllers.response.TranslationResponse;
import com.muky.toto.model.LeagueEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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
