package com.muky.toto.model;

import lombok.Getter;

@Getter
public enum LeagueEnum {
    PREMIER_LEAGUE("league.england.premier", "english", "England", 39, 2025, "פרמייר ליג"),
    ENGLISH_CHAMPIONS_LEAGUE("league.england.champions", "english", "England", 40, 2025, "צ'מפיונשיפ"),
    ENGLISH_LEAGUE_ONE("league.england.league_one", "english", "England", 41, 2025, "אנגלית ראשונה"),
    LA_LIGA("league.spain.laliga", "spanish", "Spain", 140, 2025, "ספרדית ראשונה"),
    ITALIAN_SERIE_A("league.italy.serie_a", "italian", "Italy", 135, 2025, "איטלקית ראשונה"),
    BUNDESLIGA("league.german.bundesliga", "german", "Germany", 78, 2025, "בונדסליגה"),
    FRANCE_LIGUE_1("league.french-ligue-one", "french", "France", 61, 2025, "ליג 1"),
    ISRAEL_NATIONAL_LEAGUE("league.israel.national", "hebrew", "Israel", 382, 2025, "ליגה לאומית"),
    ISRAEL_WINNER("league.israel.winner", "hebrew", "Israel", 383, 2025, "גביע המדינה Winner"),
    BELGIUM_JUPILER_PRO_LEAGUE("league.belgium.jupiler.pro", "dutch", "Belgium", 144, 2025, "בלגית ראשונה"),
    AFRICA_CUP_OF_NATIONS("league.africa.cup.of.nations", "english", "Africa", 6, 2025, "גביע אפריקה לאומות");


    private final String translationKey;
    private final String leagueLanguage;
    private final String country;
    private final int leagueId;
    private final int seasonYear;
    private final String winnerLeagueName;

    LeagueEnum(String translationKey, String leagueLanguage, String country, int leagueId, int seasonYear, String winnerLeagueName) {
        this.translationKey = translationKey;
        this.leagueLanguage = leagueLanguage;
        this.country = country;
        this.leagueId = leagueId;
        this.seasonYear = seasonYear;
        this.winnerLeagueName = winnerLeagueName;
    }
    
    public static LeagueEnum fromWinnerLeagueName(String leagueName) {
        if (leagueName == null) {
            return null;
        }
        for (LeagueEnum league : values()) {
            if (leagueName.contains(league.winnerLeagueName)) {
                return league;
            }
        }
        return null;
    }
    
    /**
     * Returns the logo URL for this league from API-Sports.io
     * @return The logo URL in the format: https://media.api-sports.io/football/leagues/{leagueId}.png
     */
    public String getLogoUrl() {
        return "https://media.api-sports.io/football/leagues/" + leagueId + ".png";
    }
}
