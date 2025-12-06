package com.muky.toto.service;

import com.muky.toto.client.BbcClient;
import com.muky.toto.client.IFATeamGamesClient;
import com.muky.toto.client.IsraelFootballAssociationClient;
import com.muky.toto.client.Sport5Client;
import com.muky.toto.config.IsraelLeagueConfig;
import com.muky.toto.model.EuropeLeagueType;
import com.muky.toto.model.IsraelLeagueType;
import com.muky.toto.model.LeagueEnum;
import com.muky.toto.model.TeamGamesEntry;
import com.muky.toto.model.TeamScoreEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LeagueService {

    private final BbcClient bbcClient;
    private final Sport5Client sport5Client;
    private final IsraelFootballAssociationClient israelFootballAssociationClient;
    private final IFATeamGamesClient iFAteamGamesClient;
    private final String seasonId;
    private volatile List<TeamScoreEntry> allTeamsCache;
    private volatile Map<String, TeamScoreEntry> teamNameToScoreEntryMap;
    private volatile Map<LeagueEnum, List<TeamScoreEntry>> leagueToTeamsMap;

    public LeagueService(BbcClient bbcClient, Sport5Client sport5Client, 
                        IsraelFootballAssociationClient israelFootballAssociationClient,
                        IsraelLeagueConfig israelLeagueConfig,
                        IFATeamGamesClient iFAteamGamesClient) {
        this.bbcClient = bbcClient;
        this.sport5Client = sport5Client;
        this.israelFootballAssociationClient = israelFootballAssociationClient;
        this.iFAteamGamesClient = iFAteamGamesClient;

        // Initialize season ID from configuration
        this.seasonId = String.valueOf(israelLeagueConfig.getSeasonId());
        
        log.info("LeagueService initialized - data will be loaded on first request");
    }

    public List<TeamScoreEntry> getAllTeams() {
        if (allTeamsCache != null) {
            return allTeamsCache;
        }

        log.info("First call to getAllTeams - loading all league data");
        allTeamsCache = loadAllTeams();
        teamNameToScoreEntryMap = allTeamsCache.stream().collect(Collectors.toMap(x -> x.getTeam(), x -> x));
        leagueToTeamsMap = allTeamsCache.stream().collect(Collectors.groupingBy(x -> x.getLeagueEnum()));
        return allTeamsCache;


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
        return sport5Client.getLeagueTable(IsraelLeagueType.NATIONAL_LEAGUE.getLeagueEnum());
    }

    public List<TeamScoreEntry> getIsraelLeagueScoreBoard(IsraelLeagueType leagueType) throws IOException {
        return israelFootballAssociationClient.getLigaScoreBoard(leagueType, seasonId);
    }

    public List<TeamGamesEntry> getTeamGames(String name) throws IOException {
        TeamScoreEntry teamScoreEntry = teamNameToScoreEntryMap.get(name);
        if (teamScoreEntry == null) {
            throw new IOException("Team not found: " + name);
        }

        return iFAteamGamesClient.getGameList(teamScoreEntry.getTeamId(), seasonId);
    }

    public TeamScoreEntry getTeamScore(String name) throws IOException {
        return Optional.ofNullable(teamNameToScoreEntryMap.get(name)).orElseThrow(() -> new IOException("Team not found: " + name));
    }

    public List<TeamScoreEntry> getLeagueScoreBoard(LeagueEnum leagueEnum) {
        return Optional.ofNullable(leagueToTeamsMap.get(leagueEnum)).orElseThrow(() -> new RuntimeException("League not found: " + leagueEnum));
    }
}
