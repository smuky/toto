package com.muky.toto.service;

import com.muky.toto.cache.MemoryCache;
import com.muky.toto.client.BbcClient;
import com.muky.toto.client.IFATeamGamesClient;
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
    private final IFATeamGamesClient iFAteamGamesclient;
    private final Map<String, String> teamNameToIdMap;
    private final String seasonId;
    private final MemoryCache memoryCache;

    public LeagueService(BbcClient bbcClient, Sport5Client sport5Client,
                         IsraelLeagueConfig israelLeagueConfig,
                         IFATeamGamesClient iFAteamGamesclient, MemoryCache memoryCache) {
        this.bbcClient = bbcClient;
        this.sport5Client = sport5Client;
        this.iFAteamGamesclient = iFAteamGamesclient;

        // Initialize season ID from configuration
        this.seasonId = String.valueOf(israelLeagueConfig.getSeasonId());
        
        // Initialize team name to ID map from national league configuration
        this.teamNameToIdMap = populateTeamNameToIdMap(israelLeagueConfig);

        this.memoryCache = memoryCache;
    }

    @PostConstruct
    private void initAllTeamsCache() {
        log.info("Loading all teams from all leagues");
        memoryCache.initAllTeamsCache(loadAllTeams());
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

    private List<TeamScoreEntry> loadAllTeams() {
        log.info("Loading all teams from all leagues");
        List<TeamScoreEntry> allTeams = new ArrayList<>();
        
        for (EuropeLeagueType leagueType: EuropeLeagueType.values()) {
            try {
                List<TeamScoreEntry> europeLeagueScoreBoard = getEuropeLeagueScoreBoard(leagueType);
                log.info("Loaded {} teams from Europe League: {}", europeLeagueScoreBoard.size(), leagueType);
                allTeams.addAll(europeLeagueScoreBoard);
            } catch (IOException e) {
                log.error("Failed to load Europe League: " + leagueType, e);
            }
        }
        
        for (IsraelLeagueType leagueType: IsraelLeagueType.values()) {
            try {
                List<TeamScoreEntry> israelLeagueScoreBoard = getIsraelLeagueScoreBoard(leagueType);
                log.info("Loaded {} teams from Israel League: {}", israelLeagueScoreBoard.size(), leagueType);
                allTeams.addAll(israelLeagueScoreBoard);
            } catch (IOException e) {
                log.error("Failed to load Israel League: " + leagueType, e);
            }
        }
        
        log.info("Total teams loaded: {}", allTeams.size());
        return allTeams;
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
