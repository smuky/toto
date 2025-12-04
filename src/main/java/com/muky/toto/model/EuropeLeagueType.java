package com.muky.toto.model;

public enum EuropeLeagueType {
    PREMIER_LEAGUE("tables#PremierLeague"),
    SPANISH_LA_LIGA("spanish-la-liga/table"),
    ITALIAN_SERIE_A("italian-serie-a/table"),
    GERMAN_BUNDESLIGA("german-bundesliga/table");

    private final String bbcClientsuffix;
    EuropeLeagueType(String bbcClientSuffix) {
        this.bbcClientsuffix = bbcClientSuffix;
    }

    public String getBBCClientSuffix() {
        return bbcClientsuffix;
    }
}
