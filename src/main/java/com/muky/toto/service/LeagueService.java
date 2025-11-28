package com.muky.toto.service;

import com.muky.toto.client.BbcClient;
import com.muky.toto.client.IFATeamGamesClient;
import com.muky.toto.client.IsraelFootballAssociationClient;
import com.muky.toto.client.Sport5Client;
import com.muky.toto.config.IsraelLeagueConfig;
import com.muky.toto.config.LeagueConfig;
import com.muky.toto.model.LeagueType;
import com.muky.toto.model.TeamGamesEntry;
import com.muky.toto.model.TeamScoreEntry;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LeagueService {

    private final BbcClient bbcClient;
    private final Sport5Client sport5Client;
    private final IsraelFootballAssociationClient israelFootballAssociationClient;
    private final IFATeamGamesClient iFAteamGamesclient;
    private final IsraelLeagueConfig israelLeagueConfig;
    private final Map<String, String> teamNameToIdMap;
    private final String seasonId;

    public LeagueService(BbcClient bbcClient, Sport5Client sport5Client, 
                        IsraelFootballAssociationClient israelFootballAssociationClient,
                        IsraelLeagueConfig israelLeagueConfig,
                        IFATeamGamesClient iFAteamGamesclient) {
        this.bbcClient = bbcClient;
        this.sport5Client = sport5Client;
        this.israelFootballAssociationClient = israelFootballAssociationClient;
        this.iFAteamGamesclient = iFAteamGamesclient;
        this.israelLeagueConfig = israelLeagueConfig;
        
        // Initialize season ID from configuration
        this.seasonId = String.valueOf(israelLeagueConfig.getSeasonId());
        
        // Initialize team name to ID map from national league configuration
        this.teamNameToIdMap = populateTeamNameToIdMap(israelLeagueConfig);
    }

    private Map<String, String> populateTeamNameToIdMap(IsraelLeagueConfig israelLeagueConfig) {
        final Map<String, String> map = new HashMap<>();
        LeagueConfig nationalLeague = israelLeagueConfig.getLeagueByType(LeagueType.NATIONAL_LEAGUE);
        if (nationalLeague != null && nationalLeague.getTeam() != null) {
            nationalLeague.getTeam().forEach((id, name) -> map.put(name, id));
        }
        LeagueConfig winnerLeague = israelLeagueConfig.getLeagueByType(LeagueType.WINNER);
        if (winnerLeague != null && winnerLeague.getTeam() != null) {
            winnerLeague.getTeam().forEach((id, name) -> map.put(name, id));
        }
        return map;
    }

    public List<TeamScoreEntry> getEnglandPremierLeague() throws IOException {
        return bbcClient.getPremierLeagueTable();
    }

    public List<TeamScoreEntry> getIsraelPremierLeagueScoreBoard() throws IOException {
        return sport5Client.getLeagueTable();
    }

    public List<TeamScoreEntry> getIsraelLeagueScoreBoard(LeagueType leagueType) throws IOException {
        LeagueConfig league = israelLeagueConfig.getLeagueByType(leagueType);
        String leagueId = String.valueOf(league.getLeagueId());
        return israelFootballAssociationClient.getLigaScoreBoard(leagueId, seasonId);
    }

    public String getLeagueId(LeagueType type) {
        LeagueConfig league = israelLeagueConfig.getLeagueByType(type);
        return league != null ? String.valueOf(league.getLeagueId()) : null;
    }

    public List<TeamGamesEntry> getTeamGames(String name) throws IOException {
        String teamId = teamNameToIdMap.get(name);
        return iFAteamGamesclient.getGameList(teamId, seasonId);
    }


}
