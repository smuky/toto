package com.muky.toto.model;

import lombok.Getter;

@Getter
public enum EuropeLeagueType {
    PREMIER_LEAGUE("tables#PremierLeague", LeagueEnum.PREMIER_LEAGUE),
    SPANISH_LA_LIGA("spanish-la-liga/table", LeagueEnum.LA_LIGA),
    ITALIAN_SERIE_A("italian-serie-a/table", LeagueEnum.ITALIAN_SERIE_A),
    GERMAN_BUNDESLIGA("german-bundesliga/table", LeagueEnum.BUNDESLIGA),
    FRENCH_LIGUE_1("french-ligue-one/table", LeagueEnum.FRANCE_LIGUE_1);


    private final String bbcClientsuffix;
    private final LeagueEnum leagueEnum;

    EuropeLeagueType(String bbcClientSuffix, LeagueEnum leagueEnum) {
        this.bbcClientsuffix = bbcClientSuffix;
        this.leagueEnum = leagueEnum;
    }
}
