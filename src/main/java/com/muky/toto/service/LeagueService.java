package com.muky.toto.service;

import com.muky.toto.client.BbcClient;
import com.muky.toto.client.IsraelFootballAssociationClient;
import com.muky.toto.client.Sport5Client;
import com.muky.toto.model.TeamScoreEntry;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class LeagueService {

    private final BbcClient bbcClient;
    private final Sport5Client sport5Client;

    private final IsraelFootballAssociationClient israelFootballAssociationClient;

    public LeagueService(BbcClient bbcClient, Sport5Client sport5Client, IsraelFootballAssociationClient israelFootballAssociationClient) {
        this.bbcClient = bbcClient;
        this.sport5Client = sport5Client;
        this.israelFootballAssociationClient = israelFootballAssociationClient;
    }

    public List<TeamScoreEntry> getLeagueInformation() throws IOException {
        return bbcClient.getPremierLeagueTable();
    }

    public List<TeamScoreEntry> getIsraelPremierLeagueScoreBoard() throws IOException {
        return sport5Client.getLeagueTable();
    }

    public List<TeamScoreEntry> getIsraelNationalLeagueScoreBoard() throws IOException {
        return israelFootballAssociationClient.getNationalLeague();
    }


}
