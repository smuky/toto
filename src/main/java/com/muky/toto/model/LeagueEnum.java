package com.muky.toto.model;

import lombok.Getter;

@Getter
public enum LeagueEnum {
    PREMIER_LEAGUE("league.england.premier", "english", "England", 39, 2025),
    ENGLISH_CHAMPIONS_LEAGUE("league.england.champions", "english", "England", 40, 2025),
    LA_LIGA("league.spain.laliga", "spanish", "Spain", 140, 2025),
    ITALIAN_SERIE_A("league.italy.serie_a", "italian", "Italy", 135, 2025),
    BUNDESLIGA("league.german.bundesliga", "german", "Germany", 78, 2025),
    FRANCE_LIGUE_1("league.french-ligue-one", "french", "France", 61, 2025),
    ISRAEL_NATIONAL_LEAGUE("league.israel.national", "hebrew", "Israel", 382, 2025),
    ISRAEL_WINNER("league.israel.winner", "hebrew", "Israel", 383, 2025);


    private final String translationKey;
    private final String leagueLanguage;
    private final String country;
    private final int leagueId;
    private final int seasonYear;

    LeagueEnum(String translationKey, String leagueLanguage, String country, int leagueId, int seasonYear) {
        this.translationKey = translationKey;
        this.leagueLanguage = leagueLanguage;
        this.country = country;
        this.leagueId = leagueId;
        this.seasonYear = seasonYear;
    }
}
