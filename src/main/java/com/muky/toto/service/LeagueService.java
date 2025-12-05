package com.muky.toto.service;

import com.muky.toto.client.BbcClient;
import com.muky.toto.client.IFATeamGamesClient;
import com.muky.toto.client.IsraelFootballAssociationClient;
import com.muky.toto.client.Sport5Client;
import com.muky.toto.config.IsraelLeagueConfig;
import com.muky.toto.config.LeagueConfig;
import com.muky.toto.model.EuropeLeagueType;
import com.muky.toto.model.IsraelLeagueType;
import com.muky.toto.model.TeamGamesEntry;
import com.muky.toto.model.TeamScoreEntry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LeagueService {

    private final BbcClient bbcClient;
    private final Sport5Client sport5Client;
    private final IsraelFootballAssociationClient israelFootballAssociationClient;
    private final IFATeamGamesClient iFAteamGamesclient;
    private final Map<String, String> teamNameToIdMap;
    private final String seasonId;
    private List<TeamScoreEntry> allTeams;

    public LeagueService(BbcClient bbcClient, Sport5Client sport5Client, 
                        IsraelFootballAssociationClient israelFootballAssociationClient,
                        IsraelLeagueConfig israelLeagueConfig,
                        IFATeamGamesClient iFAteamGamesclient) {
        this.bbcClient = bbcClient;
        this.sport5Client = sport5Client;
        this.israelFootballAssociationClient = israelFootballAssociationClient;
        this.iFAteamGamesclient = iFAteamGamesclient;

        // Initialize season ID from configuration
        this.seasonId = String.valueOf(israelLeagueConfig.getSeasonId());
        
        // Initialize team name to ID map from national league configuration
        this.teamNameToIdMap = populateTeamNameToIdMap(israelLeagueConfig);
        log.info("Start Initialize Europe Leagues");

    }

    @PostConstruct
    public void init() {
        allTeams = initAllTeams();
    }

    public List<TeamScoreEntry> getAllTeams() {
        return allTeams;
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

    private List<TeamScoreEntry> initAllTeams() {
        log.info("Start Initialize Europe Leagues");
        List<TeamScoreEntry> allTeams = new ArrayList<>();
        for (EuropeLeagueType leagueType: EuropeLeagueType.values()) {
            try {
                List<TeamScoreEntry> europeLeagueScoreBoard = getEuropeLeagueScoreBoard(leagueType);
                log.info("Europe League: " + leagueType + " initialized");
                allTeams.addAll(europeLeagueScoreBoard);
            } catch (IOException e) {
                log.error("Failed to initialize Europe League: " + leagueType, e);
            }
        }
        for (IsraelLeagueType leagueType: IsraelLeagueType.values()) {
            List<TeamScoreEntry> israelLeagueScoreBoard = null;
            try {
                israelLeagueScoreBoard = getIsraelLeagueScoreBoard(leagueType);
                log.info("Israel League: " + leagueType + " initialized");
            } catch (IOException e) {
                log.error("Failed to initialize Israel League: " + leagueType, e);
            }
            allTeams.addAll(israelLeagueScoreBoard);

        }
        return allTeams;

    }

    public List<TeamScoreEntry> getEuropeLeagueScoreBoard(EuropeLeagueType leagueType) throws IOException {
        return bbcClient.getLeagueScoreBoard(leagueType);
    }

    public List<TeamScoreEntry> getIsraelPremierLeagueScoreBoard() throws IOException {
        return sport5Client.getLeagueTable(IsraelLeagueType.NATIONAL_LEAGUE.getLeagueEnum());
    }

    public List<TeamScoreEntry> getIsraelLeagueScoreBoard(IsraelLeagueType leagueType) throws IOException {
        return israelFootballAssociationClient.getLigaScoreBoard(leagueType, seasonId);
    }

    public List<TeamGamesEntry> getTeamGames(String name) throws IOException {
        String teamId = teamNameToIdMap.get(name);
        return iFAteamGamesclient.getGameList(teamId, seasonId);
    }


}
