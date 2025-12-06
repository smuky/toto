package com.muky.toto.cache;

import com.muky.toto.model.LeagueEnum;
import com.muky.toto.model.TeamScoreEntry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MemoryCache {

    private volatile List<TeamScoreEntry> allTeamsCache;
    private volatile Map<String, TeamScoreEntry> teamNameToScoreEntryMap;
    private volatile Map<LeagueEnum, List<TeamScoreEntry>> leagueToTeamsMap;

    public void initAllTeamsCache(List<TeamScoreEntry> allTeamsCache) {
        this.allTeamsCache = allTeamsCache;
        this.teamNameToScoreEntryMap = allTeamsCache.stream().collect(Collectors.toMap(TeamScoreEntry::getTeam, x -> x));
        leagueToTeamsMap = allTeamsCache.stream().collect(Collectors.groupingBy(TeamScoreEntry::getLeagueEnum));
    }

    public List<TeamScoreEntry> getAllTeams() {
        return allTeamsCache;
    }

    public TeamScoreEntry getTeamScoreEntry(String teamName) {
        return teamNameToScoreEntryMap.get(teamName);
    }

    public List<TeamScoreEntry> getTeamsByLeague(LeagueEnum leagueEnum) {
        return leagueToTeamsMap.get(leagueEnum);
    }
}
