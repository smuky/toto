package com.muky.toto.model;

import lombok.Getter;

@Getter
public enum LeagueEnum {
    PREMIER_LEAGUE("league.england.premier"),
    LA_LIGA("league.spain.laliga"),
    ITALIAN_SERIE_A("league.italy.serie_a"),
    BUNDESLIGA("league.german.bundesliga"),
    FRANCE_LIGUE_1("league.french-ligue-one"),
    ENGLISH_CHAMPIONS_LEAGUE("league.england.champions"),
    ISRAEL_NATIONAL_LEAGUE("league.israel.national"),
    ISRAEL_WINNER("league.israel.winner");


    private final String translationKey;
    LeagueEnum(String translationKey) {
        this.translationKey = translationKey;
    }
}
