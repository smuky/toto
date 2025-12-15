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
        
        // Israeli National League teams
        TEAM_ID_TO_TRANSLATION_KEY.put(4502, "team.maccabi_bnei_raina");
        TEAM_ID_TO_TRANSLATION_KEY.put(4503, "team.maccabi_herzliya");
        TEAM_ID_TO_TRANSLATION_KEY.put(4497, "team.hapoel_kfar_saba");
        TEAM_ID_TO_TRANSLATION_KEY.put(4491, "team.hapoel_rishon_lezion");
        TEAM_ID_TO_TRANSLATION_KEY.put(6160, "team.hapoel_kfar_shalem");
        TEAM_ID_TO_TRANSLATION_KEY.put(4493, "team.kafr_qasim");
        TEAM_ID_TO_TRANSLATION_KEY.put(4489, "team.hapoel_ramat_gan");
        TEAM_ID_TO_TRANSLATION_KEY.put(4482, "team.hapoel_acre");
        TEAM_ID_TO_TRANSLATION_KEY.put(6179, "team.ironi_modiin");
        TEAM_ID_TO_TRANSLATION_KEY.put(20105, "team.kiryat_yam_sc");
        TEAM_ID_TO_TRANSLATION_KEY.put(4509, "team.hapoel_raanana");
        TEAM_ID_TO_TRANSLATION_KEY.put(6192, "team.maccabi_kabilio_jaffa");
        TEAM_ID_TO_TRANSLATION_KEY.put(4508, "team.bnei_yehuda");
        TEAM_ID_TO_TRANSLATION_KEY.put(4483, "team.hapoel_afula");
        TEAM_ID_TO_TRANSLATION_KEY.put(4487, "team.hapoel_nazareth_illit");

        // Premier League teams
        TEAM_ID_TO_TRANSLATION_KEY.put(33, "team.manchester_united");
        TEAM_ID_TO_TRANSLATION_KEY.put(34, "team.newcastle");
        TEAM_ID_TO_TRANSLATION_KEY.put(40, "team.liverpool");
        TEAM_ID_TO_TRANSLATION_KEY.put(42, "team.arsenal");
        TEAM_ID_TO_TRANSLATION_KEY.put(47, "team.tottenham");
        TEAM_ID_TO_TRANSLATION_KEY.put(50, "team.manchester_city");
        TEAM_ID_TO_TRANSLATION_KEY.put(49, "team.chelsea");
        TEAM_ID_TO_TRANSLATION_KEY.put(66, "team.aston_villa");
        TEAM_ID_TO_TRANSLATION_KEY.put(52, "team.crystal_palace");
        TEAM_ID_TO_TRANSLATION_KEY.put(746, "team.sunderland");
        TEAM_ID_TO_TRANSLATION_KEY.put(45, "team.everton");
        TEAM_ID_TO_TRANSLATION_KEY.put(51, "team.brighton");
        TEAM_ID_TO_TRANSLATION_KEY.put(36, "team.fulham");
        TEAM_ID_TO_TRANSLATION_KEY.put(55, "team.brentford");
        TEAM_ID_TO_TRANSLATION_KEY.put(35, "team.bournemouth");
        TEAM_ID_TO_TRANSLATION_KEY.put(65, "team.nottingham_forest");
        TEAM_ID_TO_TRANSLATION_KEY.put(63, "team.leeds");
        TEAM_ID_TO_TRANSLATION_KEY.put(48, "team.west_ham");
        TEAM_ID_TO_TRANSLATION_KEY.put(44, "team.burnley");
        TEAM_ID_TO_TRANSLATION_KEY.put(39, "team.wolves");
        
        // English Championship teams
        TEAM_ID_TO_TRANSLATION_KEY.put(46, "team.leicester");
        TEAM_ID_TO_TRANSLATION_KEY.put(1359, "team.luton");
        TEAM_ID_TO_TRANSLATION_KEY.put(57, "team.ipswich");
        TEAM_ID_TO_TRANSLATION_KEY.put(1346, "team.middlesbrough");
        TEAM_ID_TO_TRANSLATION_KEY.put(64, "team.stoke");
        TEAM_ID_TO_TRANSLATION_KEY.put(61, "team.preston");
        TEAM_ID_TO_TRANSLATION_KEY.put(59, "team.millwall");
        TEAM_ID_TO_TRANSLATION_KEY.put(1345, "team.coventry");
        TEAM_ID_TO_TRANSLATION_KEY.put(72, "team.qpr");
        TEAM_ID_TO_TRANSLATION_KEY.put(41, "team.southampton");
        TEAM_ID_TO_TRANSLATION_KEY.put(56, "team.bristol_city");
        TEAM_ID_TO_TRANSLATION_KEY.put(54, "team.birmingham");
        TEAM_ID_TO_TRANSLATION_KEY.put(38, "team.watford");
        TEAM_ID_TO_TRANSLATION_KEY.put(1837, "team.wrexham");
        TEAM_ID_TO_TRANSLATION_KEY.put(69, "team.derby");
        TEAM_ID_TO_TRANSLATION_KEY.put(60, "team.west_brom");
        TEAM_ID_TO_TRANSLATION_KEY.put(1335, "team.charlton");
        TEAM_ID_TO_TRANSLATION_KEY.put(62, "team.sheffield_utd");
        TEAM_ID_TO_TRANSLATION_KEY.put(76, "team.swansea");
        TEAM_ID_TO_TRANSLATION_KEY.put(67, "team.blackburn");
        TEAM_ID_TO_TRANSLATION_KEY.put(1355, "team.portsmouth");
        TEAM_ID_TO_TRANSLATION_KEY.put(1338, "team.oxford_united");
        TEAM_ID_TO_TRANSLATION_KEY.put(71, "team.norwich");
        TEAM_ID_TO_TRANSLATION_KEY.put(74, "team.sheffield_wednesday");
        
        // La Liga teams
        TEAM_ID_TO_TRANSLATION_KEY.put(529, "team.barcelona");
        TEAM_ID_TO_TRANSLATION_KEY.put(541, "team.real_madrid");
        TEAM_ID_TO_TRANSLATION_KEY.put(530, "team.atletico_madrid");
        TEAM_ID_TO_TRANSLATION_KEY.put(533, "team.villarreal");
        TEAM_ID_TO_TRANSLATION_KEY.put(540, "team.espanyol");
        TEAM_ID_TO_TRANSLATION_KEY.put(543, "team.real_betis");
        TEAM_ID_TO_TRANSLATION_KEY.put(531, "team.athletic_club");
        TEAM_ID_TO_TRANSLATION_KEY.put(538, "team.celta_vigo");
        TEAM_ID_TO_TRANSLATION_KEY.put(536, "team.sevilla");
        TEAM_ID_TO_TRANSLATION_KEY.put(546, "team.getafe");
        TEAM_ID_TO_TRANSLATION_KEY.put(797, "team.elche");
        TEAM_ID_TO_TRANSLATION_KEY.put(542, "team.alaves");
        TEAM_ID_TO_TRANSLATION_KEY.put(728, "team.rayo_vallecano");
        TEAM_ID_TO_TRANSLATION_KEY.put(798, "team.mallorca");
        TEAM_ID_TO_TRANSLATION_KEY.put(548, "team.real_sociedad");
        TEAM_ID_TO_TRANSLATION_KEY.put(727, "team.osasuna");
        TEAM_ID_TO_TRANSLATION_KEY.put(532, "team.valencia");
        TEAM_ID_TO_TRANSLATION_KEY.put(547, "team.girona");
        TEAM_ID_TO_TRANSLATION_KEY.put(718, "team.oviedo");
        TEAM_ID_TO_TRANSLATION_KEY.put(539, "team.levante");
        
        // Serie A teams
        TEAM_ID_TO_TRANSLATION_KEY.put(489, "team.ac_milan");
        TEAM_ID_TO_TRANSLATION_KEY.put(505, "team.inter");
        TEAM_ID_TO_TRANSLATION_KEY.put(492, "team.napoli");
        TEAM_ID_TO_TRANSLATION_KEY.put(497, "team.roma");
        TEAM_ID_TO_TRANSLATION_KEY.put(496, "team.juventus");
        TEAM_ID_TO_TRANSLATION_KEY.put(500, "team.bologna");
        TEAM_ID_TO_TRANSLATION_KEY.put(895, "team.como");
        TEAM_ID_TO_TRANSLATION_KEY.put(487, "team.lazio");
        TEAM_ID_TO_TRANSLATION_KEY.put(488, "team.sassuolo");
        TEAM_ID_TO_TRANSLATION_KEY.put(494, "team.udinese");
        TEAM_ID_TO_TRANSLATION_KEY.put(520, "team.cremonese");
        TEAM_ID_TO_TRANSLATION_KEY.put(499, "team.atalanta");
        TEAM_ID_TO_TRANSLATION_KEY.put(503, "team.torino");
        TEAM_ID_TO_TRANSLATION_KEY.put(867, "team.lecce");
        TEAM_ID_TO_TRANSLATION_KEY.put(490, "team.cagliari");
        TEAM_ID_TO_TRANSLATION_KEY.put(495, "team.genoa");
        TEAM_ID_TO_TRANSLATION_KEY.put(523, "team.parma");
        TEAM_ID_TO_TRANSLATION_KEY.put(504, "team.verona");
        TEAM_ID_TO_TRANSLATION_KEY.put(801, "team.pisa");
        TEAM_ID_TO_TRANSLATION_KEY.put(502, "team.fiorentina");
        
        // Bundesliga teams
        TEAM_ID_TO_TRANSLATION_KEY.put(157, "team.bayern_munich");
        TEAM_ID_TO_TRANSLATION_KEY.put(173, "team.rb_leipzig");
        TEAM_ID_TO_TRANSLATION_KEY.put(165, "team.borussia_dortmund");
        TEAM_ID_TO_TRANSLATION_KEY.put(168, "team.bayer_leverkusen");
        TEAM_ID_TO_TRANSLATION_KEY.put(167, "team.hoffenheim");
        TEAM_ID_TO_TRANSLATION_KEY.put(172, "team.vfb_stuttgart");
        TEAM_ID_TO_TRANSLATION_KEY.put(169, "team.eintracht_frankfurt");
        TEAM_ID_TO_TRANSLATION_KEY.put(182, "team.union_berlin");
        TEAM_ID_TO_TRANSLATION_KEY.put(160, "team.sc_freiburg");
        TEAM_ID_TO_TRANSLATION_KEY.put(192, "team.fc_koln");
        TEAM_ID_TO_TRANSLATION_KEY.put(163, "team.borussia_monchengladbach");
        TEAM_ID_TO_TRANSLATION_KEY.put(162, "team.werder_bremen");
        TEAM_ID_TO_TRANSLATION_KEY.put(161, "team.vfl_wolfsburg");
        TEAM_ID_TO_TRANSLATION_KEY.put(175, "team.hamburger_sv");
        TEAM_ID_TO_TRANSLATION_KEY.put(170, "team.fc_augsburg");
        TEAM_ID_TO_TRANSLATION_KEY.put(186, "team.fc_st_pauli");
        TEAM_ID_TO_TRANSLATION_KEY.put(180, "team.fc_heidenheim");
        TEAM_ID_TO_TRANSLATION_KEY.put(164, "team.fsv_mainz_05");
        
        // Ligue 1 teams
        TEAM_ID_TO_TRANSLATION_KEY.put(85, "team.psg");
        TEAM_ID_TO_TRANSLATION_KEY.put(82, "team.lens");
        TEAM_ID_TO_TRANSLATION_KEY.put(81, "team.marseille");
        TEAM_ID_TO_TRANSLATION_KEY.put(79, "team.lille");
        TEAM_ID_TO_TRANSLATION_KEY.put(80, "team.lyon");
        TEAM_ID_TO_TRANSLATION_KEY.put(94, "team.rennes");
        TEAM_ID_TO_TRANSLATION_KEY.put(95, "team.strasbourg");
        TEAM_ID_TO_TRANSLATION_KEY.put(96, "team.toulouse");
        TEAM_ID_TO_TRANSLATION_KEY.put(91, "team.monaco");
        TEAM_ID_TO_TRANSLATION_KEY.put(77, "team.angers");
        TEAM_ID_TO_TRANSLATION_KEY.put(106, "team.stade_brestois_29");
        TEAM_ID_TO_TRANSLATION_KEY.put(97, "team.lorient");
        TEAM_ID_TO_TRANSLATION_KEY.put(84, "team.nice");
        TEAM_ID_TO_TRANSLATION_KEY.put(114, "team.paris_fc");
        TEAM_ID_TO_TRANSLATION_KEY.put(111, "team.le_havre");
        TEAM_ID_TO_TRANSLATION_KEY.put(108, "team.auxerre");
        TEAM_ID_TO_TRANSLATION_KEY.put(83, "team.nantes");
        TEAM_ID_TO_TRANSLATION_KEY.put(112, "team.metz");
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
