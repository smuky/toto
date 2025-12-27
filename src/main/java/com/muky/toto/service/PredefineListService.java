package com.muky.toto.service;

import com.muky.toto.client.api_football.Fixture;
import com.muky.toto.controllers.requests.WinnerGamesRequest;
import com.muky.toto.model.LeagueEnum;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PredefineListService {

    private final ApiFootballService apiFootballService;

    public PredefineListService(ApiFootballService apiFootballService) {
        this.apiFootballService = apiFootballService;
    }

    public void storePredefinedList(WinnerGamesRequest request) {

        for (WinnerGamesRequest.GameData gameData : request.getGames()) {
            int gameType = gameData.getGameType();
            int drawNumber = gameData.getDrawNumber();
            List<WinnerGamesRequest.Row> games = gameData.getRows();

            Map<LeagueEnum, List<Fixture>> fixturesByLeague = getFixturesByLeague(games);

            List<String> fixtureIds = new ArrayList<>();

            for (WinnerGamesRequest.Row game : games) {
                fixtureIds.add(getFixtureId(fixturesByLeague, game));
            }

            String result = fixtureIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining("-"));
            log.info(result);
        }
    }

    private Map<LeagueEnum, List<Fixture>> getFixturesByLeague(List<WinnerGamesRequest.Row> games) {
        Map<LeagueEnum, List<Fixture>> fixturesByLeague = new HashMap<>();
        Set<String> uniqueLeagues = games.stream()
                .map(WinnerGamesRequest.Row::getLeague)
                .collect(Collectors.toSet());

        for (String leagueName : uniqueLeagues) {
            LeagueEnum leagueEnum = LeagueEnum.fromWinnerLeagueName(leagueName);
            if (leagueEnum != null && !fixturesByLeague.containsKey(leagueEnum)) {
                log.info("Fetching fixtures for league: {} ({})", leagueName, leagueEnum);
                List<Fixture> fixtures = apiFootballService.getNextFixtures(leagueEnum, 30);
                apiFootballService.populateFixtureTeamDisplayNames(fixtures, "he");
                fixturesByLeague.put(leagueEnum, fixtures);
                log.info("Fetched {} fixtures for league: {}", fixtures.size(), leagueEnum);
            }
        }

        return fixturesByLeague;
    }

    
    private static String getFixtureId(Map<LeagueEnum, List<Fixture>> fixturesByLeague, WinnerGamesRequest.Row game) {
        LeagueEnum leagueEnum = LeagueEnum.fromWinnerLeagueName(game.getLeague());
        if (leagueEnum == null) {
            log.warn("Could not identify league for: {}", game.getLeague());
            String fixtureId = "unknownLeagueForGame:" + game.getLeague();
            return fixtureId;
        }

        List<Fixture> fixtures = fixturesByLeague.get(leagueEnum);
        if (fixtures == null) {
            log.warn("No fixtures found for league: {}", leagueEnum);
            String fixtureId = "unknownfixturesforLeague:" + leagueEnum;
            return fixtureId;
        }

        Optional<Fixture> matchedFixture = getMatchedFixture(game, fixtures);

        if (matchedFixture.isPresent()) {
            String fixtureId = matchedFixture.get().getFixture().getId()+"";
            log.info("Matched game: {} vs {} -> fixture ID: {}", game.getTeamA(), game.getTeamB(), fixtureId);
            return  fixtureId;

        } else {
            log.warn("Could not find fixture for game: {} vs {} in league: {}",
                    game.getTeamA(), game.getTeamB(), game.getLeague());
            String fixtureId =
                    "unknownFixtureforGame:" + game.getTeamA() + "VS" + game.getTeamB() + "@" +  game.getLeague();
            return fixtureId;
        }
    }
}
