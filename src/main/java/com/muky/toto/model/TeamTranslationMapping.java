package com.muky.toto.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps API-Football team IDs to translation keys in messages.properties
 */
public class TeamTranslationMapping {
    
    private static final Map<Integer, String> TEAM_ID_TO_TRANSLATION_KEY = new HashMap<>();
    
    static {
        // Israeli Premier League teams
        TEAM_ID_TO_TRANSLATION_KEY.put(604, "team.maccabi_tel_aviv");
        TEAM_ID_TO_TRANSLATION_KEY.put(4195, "team.maccabi_haifa");
        TEAM_ID_TO_TRANSLATION_KEY.put(563, "team.hapoel_beer_sheva");
        TEAM_ID_TO_TRANSLATION_KEY.put(6186, "team.maccabi_bnei_reineh");
        TEAM_ID_TO_TRANSLATION_KEY.put(4481, "team.bnei_sakhnin");
        TEAM_ID_TO_TRANSLATION_KEY.put(4486, "team.hapoel_jerusalem");
        TEAM_ID_TO_TRANSLATION_KEY.put(2253, "team.hapoel_haifa");
        TEAM_ID_TO_TRANSLATION_KEY.put(4495, "team.maccabi_petah_tikva");
        TEAM_ID_TO_TRANSLATION_KEY.put(4488, "team.hapoel_petah_tikva");
        TEAM_ID_TO_TRANSLATION_KEY.put(4505, "team.maccabi_netanya");
        TEAM_ID_TO_TRANSLATION_KEY.put(4500, "team.hapoel_hadera");
        TEAM_ID_TO_TRANSLATION_KEY.put(4501, "team.hapoel_tel_aviv");
        TEAM_ID_TO_TRANSLATION_KEY.put(657, "team.beitar_jerusalem");
        TEAM_ID_TO_TRANSLATION_KEY.put(4507, "team.ashdod");
        TEAM_ID_TO_TRANSLATION_KEY.put(4510, "team.hapoel_kiryat_shmona");
        TEAM_ID_TO_TRANSLATION_KEY.put(6181, "team.ironi_tiberias");


        // Add more teams as needed (Premier League, La Liga, etc.)
        TEAM_ID_TO_TRANSLATION_KEY.put(33, "team.manchester_united");
        TEAM_ID_TO_TRANSLATION_KEY.put(34, "team.newcastle");
        TEAM_ID_TO_TRANSLATION_KEY.put(40, "team.liverpool");
        TEAM_ID_TO_TRANSLATION_KEY.put(42, "team.arsenal");
        TEAM_ID_TO_TRANSLATION_KEY.put(47, "team.tottenham");
        TEAM_ID_TO_TRANSLATION_KEY.put(50, "team.manchester_city");
        TEAM_ID_TO_TRANSLATION_KEY.put(49, "team.chelsea");
        // ... add more teams
    }
    
    /**
     * Get translation key for a team ID
     * @param teamId API-Football team ID
     * @return Translation key or null if not found
     */
    public static String getTranslationKey(int teamId) {
        return TEAM_ID_TO_TRANSLATION_KEY.get(teamId);
    }
    
    /**
     * Check if a team has a translation
     * @param teamId API-Football team ID
     * @return true if translation exists
     */
    public static boolean hasTranslation(int teamId) {
        return TEAM_ID_TO_TRANSLATION_KEY.containsKey(teamId);
    }
}
