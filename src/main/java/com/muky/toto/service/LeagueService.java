package com.muky.toto.service;

import com.muky.toto.client.BbcClient;
import com.muky.toto.client.IFATeamGamesClient;
import com.muky.toto.client.Sport5Client;
import com.muky.toto.config.IsraelLeagueConfig;
import com.muky.toto.config.LeagueConfig;
import com.muky.toto.model.EuropeLeagueType;
import com.muky.toto.model.IsraelLeagueType;
import com.muky.toto.model.TeamGamesEntry;
import com.muky.toto.model.TeamScoreEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LeagueService {

    private final BbcClient bbcClient;
    private final Sport5Client sport5Client;
    private final IFATeamGamesClient iFAteamGamesclient;
    private final Map<String, String> teamNameToIdMap;
    private final String seasonId;

    public LeagueService(BbcClient bbcClient, Sport5Client sport5Client,
                         IsraelLeagueConfig israelLeagueConfig,
                         IFATeamGamesClient iFAteamGamesclient) {
        this.bbcClient = bbcClient;
        this.sport5Client = sport5Client;
        this.iFAteamGamesclient = iFAteamGamesclient;

        // Initialize season ID from configuration
        this.seasonId = String.valueOf(israelLeagueConfig.getSeasonId());
        
        // Initialize team name to ID map from national league configuration
        this.teamNameToIdMap = populateTeamNameToIdMap(israelLeagueConfig);
    }

    private Map<String, String> populateTeamNameToIdMap(IsraelLeagueConfig israelLeagueConfig) {
        final Map<String, String> map = new HashMap<>();
        LeagueConfig nationalLeague = israelLeagueConfig.getLeagueByType(IsraelLeagueType.NATIONAL_LEAGUE);
        if (nationalLeague != null && nationalLeague.getTeam() != null) {
            nationalLeague.getTeam().forEach((id, name) -> map.put(name, id));
        }
        LeagueConfig winnerLeague = israelLeagueConfig.getLeagueByType(IsraelLeagueType.WINNER);
        if (winnerLeague != null && winnerLeague.getTeam() != null) {
            winnerLeague.getTeam().forEach((id, name) -> map.put(name, id));
        }
        return map;
    }

    public List<TeamScoreEntry> getEuropeLeagueScoreBoard(EuropeLeagueType leagueType) throws IOException {
        return bbcClient.getLeagueScoreBoard(leagueType);
    }

    public List<TeamScoreEntry> getIsraelPremierLeagueScoreBoard() throws IOException {
        return sport5Client.getLeagueTable(IsraelLeagueType.NATIONAL_LEAGUE);
    }

    public List<TeamScoreEntry> getIsraelLeagueScoreBoard(IsraelLeagueType leagueType) throws IOException {
        return sport5Client.getLeagueTable(leagueType);
    }

    public List<TeamGamesEntry> getTeamGames(String name) throws IOException {
        String teamId = teamNameToIdMap.get(name);
        return iFAteamGamesclient.getGameList(teamId, seasonId);
    }


}
