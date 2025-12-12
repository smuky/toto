package com.muky.toto.model;

import lombok.Getter;

@Getter
public enum LeagueEnum {
    PREMIER_LEAGUE("league.england.premier", "english"),
    ENGLISH_CHAMPIONS_LEAGUE("league.england.champions", "english"),
    LA_LIGA("league.spain.laliga", "spanish"),
    ITALIAN_SERIE_A("league.italy.serie_a", "italian"),
    BUNDESLIGA("league.german.bundesliga", "german"),
    FRANCE_LIGUE_1("league.french-ligue-one", "french"),
    ISRAEL_NATIONAL_LEAGUE("league.israel.national", "hebrew"),
    ISRAEL_WINNER("league.israel.winner", "henrew");


    private final String translationKey;
    private final String leagueLanguage;

    LeagueEnum(String translationKey, String leagueLanguage) {
        this.translationKey = translationKey;
        this.leagueLanguage = leagueLanguage;
    }
}
