package com.muky.toto.service;

import com.muky.toto.model.LeagueEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

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
}
