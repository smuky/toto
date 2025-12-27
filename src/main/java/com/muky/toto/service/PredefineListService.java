package com.muky.toto.service;

import com.muky.toto.client.api_football.Fixture;
import com.muky.toto.controllers.requests.WinnerGamesRequest;
import com.muky.toto.model.LeagueEnum;
import com.muky.toto.repositories.PredefineListEntity;
import com.muky.toto.repositories.PredefineListRepository;

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
    private final PredefineListRepository predefineListRepository;

    public PredefineListService(
            ApiFootballService apiFootballService,
            PredefineListRepository predefineListRepository) {
        this.apiFootballService = apiFootballService;
        this.predefineListRepository = predefineListRepository;
    }

    public String getFixtureIdsByDrawNumber(int drawNumber, int gameType, WinnerGamesRequest request) {
        List<WinnerGamesRequest.Row> games = getGames(drawNumber, gameType, request);

        if (games.isEmpty()) {
            log.warn("No games found for drawNumber: {}", drawNumber);
            return "No games found";
        }

        Map<LeagueEnum, List<Fixture>> fixturesByLeague = getFixturesByLeague(games);

        List<String> fixtureIds = new ArrayList<>();

        for (WinnerGamesRequest.Row game : games) {
            try {
                fixtureIds.add(getFixtureId(fixturesByLeague, game));
            } catch (Exception e) {
                log.error("Error getting fixture ID for game: {}", game, e);
                fixtureIds.add(e.getMessage());
            }
        }

        return fixtureIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("-"));
    }

    private static List<WinnerGamesRequest.Row> getGames(int drawNumber, int gameType, WinnerGamesRequest request) {
        return request.getGames().stream()
                .filter(game -> game.getDrawNumber() == drawNumber & game.getGameType() == gameType)
                .flatMap(game -> game.getRows().stream())
                .collect(Collectors.toList());
    }

    public void storePredefinedList(WinnerGamesRequest request) {

        for (WinnerGamesRequest.GameData gameData : request.getGames()) {
            int drawId = gameData.getDrawId();
            int gameType = gameData.getGameType();
            int drawNumber = gameData.getDrawNumber();

            String error = "";
            List<WinnerGamesRequest.Row> games = gameData.getRows();

            Map<LeagueEnum, List<Fixture>> fixturesByLeague = getFixturesByLeague(games);

            List<String> fixtureIds = new ArrayList<>();

            for (WinnerGamesRequest.Row game : games) {
                try {
                    fixtureIds.add(getFixtureId(fixturesByLeague, game));
                } catch (Exception e) {
                    log.error("Error getting fixture ID for game: {}", game, e);
                    error += e.getMessage();
                }
            }

            String result = fixtureIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining("-"));

            //store the result in the database
            saveOrUpdatePredefineList(drawId, drawNumber, gameType, result, error);
            log.info("Stored predefine list for drawNumber: {}, gameType: {}, result: {}", 
                    drawNumber, gameType, result);
        }
    }

    private void saveOrUpdatePredefineList(Integer drawId, Integer drawNumber, Integer gameType,
                                           String fixtureList, String failedDescription) {
        PredefineListEntity entity = new PredefineListEntity();

        entity.setDrawId(drawId);
        entity.setDrawNumber(drawNumber);
        entity.setGameType(gameType);
        entity.setFixtureList(fixtureList);
        entity.setFailedDescription(failedDescription.isEmpty() ? null : failedDescription);

        predefineListRepository.save(entity);
        log.info("Successfully saved predefine list with drawId: {}", entity.getDrawId());
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

    
    private String getFixtureId(Map<LeagueEnum, List<Fixture>> fixturesByLeague, WinnerGamesRequest.Row game) throws Exception{
        LeagueEnum leagueEnum = LeagueEnum.fromWinnerLeagueName(game.getLeague());
        if (leagueEnum == null) {
            throw new Exception("Could not identify league for: " + game.getLeague());
        }

        List<Fixture> fixtures = fixturesByLeague.get(leagueEnum);
        if (fixtures == null) {
            throw new Exception("No fixtures found for league: " + leagueEnum);
        }

        Optional<Fixture> matchedFixture = getMatchedFixture(game, fixtures);

        if (matchedFixture.isPresent()) {
            String fixtureId = matchedFixture.get().getFixture().getId()+"";
            log.info("Matched game: {} vs {} -> fixture ID: {}", game.getTeamA(), game.getTeamB(), fixtureId);
            return  fixtureId;

        } else {
            throw new Exception("Could not find fixture for game: " + game.getTeamA() + " vs " + game.getTeamB() + " in league: " + game.getLeague());
        }
    }

    private Optional<Fixture> getMatchedFixture(WinnerGamesRequest.Row game, List<Fixture> fixtures) {
        Optional<Fixture> matchedFixture = fixtures.stream()
                .filter(fixture -> fixture.getTeams() != null)
                .filter(fixture -> {
                    String homeDisplayName = fixture.getTeams().getHome() != null ?
                            fixture.getTeams().getHome().getDisplayName() : null;
                    String awayDisplayName = fixture.getTeams().getAway() != null ?
                            fixture.getTeams().getAway().getDisplayName() : null;

                    boolean homeMatch = homeDisplayName != null && homeDisplayName.equals(game.getTeamA());
                    boolean awayMatch = awayDisplayName != null && awayDisplayName.equals(game.getTeamB());

                    return homeMatch && awayMatch;
                })
                .findFirst();
        return matchedFixture;
    }
}
