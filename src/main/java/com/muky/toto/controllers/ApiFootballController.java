package com.muky.toto.controllers;

import com.muky.toto.client.api_football.Fixture;
import com.muky.toto.client.api_football.League;
import com.muky.toto.client.api_football.Standing;
import com.muky.toto.model.LeagueEnum;
import com.muky.toto.model.apifootball.SupportedCountriesEnum;
import com.muky.toto.service.ApiFootballService;
import com.muky.toto.service.TranslationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ApiFootballController implements ApiFootballApi {

    private final ApiFootballService apiFootballService;
    private final TranslationService translationService;

    @Override
    public ResponseEntity<List<League>> getLeagues(SupportedCountriesEnum country) {
        log.info("Getting leagues for country: {}", country);
        List<League> leagues = apiFootballService.getLeagues(country);
        log.info("Retrieved {} leagues for country: {}", leagues.size(), country);
        return ResponseEntity.ok(leagues);
    }

    @Override
    public ResponseEntity<Standing> getStandings(LeagueEnum leagueEnum, String language) {
        log.info("Getting standings for league {} with language: {} ", leagueEnum.name(), language);
        Standing standings = apiFootballService.getStanding(leagueEnum);

        populateTeamDisplayNames(standings, language);

        log.info("Retrieved standings for league {} with language: {}", leagueEnum.name(), language);
        return ResponseEntity.ok(standings);
    }

    @Override
    public ResponseEntity<List<Fixture>> getNextFixtures(LeagueEnum leagueEnum, int next, String language) {
        log.info("Getting next {} fixtures for league {} with language: {}", next, leagueEnum.name(), language);
        List<Fixture> fixtures = apiFootballService.getNextFixtures(leagueEnum, next);
        
        populateFixtureTeamDisplayNames(fixtures, language);
        
        log.info("Retrieved {} fixtures for league {} with language: {}", fixtures.size(), leagueEnum.name(), language);
        return ResponseEntity.ok(fixtures);
    }

    @Override
    public ResponseEntity<Object> getPredictions(int fixture) {
        log.info("Getting predictions for fixture: {}", fixture);
        Object predictions = apiFootballService.getPredictions(fixture);
        log.info("Retrieved predictions for fixture: {}", fixture);
        return ResponseEntity.ok(predictions);
    }

    private void populateTeamDisplayNames(Standing standings, String languageCode) {
        if (standings != null && standings.getLeague() != null && standings.getLeague().getStandings() != null) {
            standings.getLeague().getStandings().forEach(standingList ->
                standingList.forEach(entry -> {
                    Standing.Team team = entry.getTeam();
                    if (team != null) {
                        String translatedName = translationService.getTeamName(
                            team.getId(),
                            team.getName(),
                            languageCode
                        );
                        team.setDisplayName(translatedName);
                        log.debug("Set displayName for team ID {}: {}", team.getId(), translatedName);
                    }
                })
            );
        }
    }

    private void populateFixtureTeamDisplayNames(List<Fixture> fixtures, String languageCode) {
        if (fixtures != null) {
            fixtures.forEach(fixture -> {
                if (fixture.getTeams() != null) {
                    Fixture.Team homeTeam = fixture.getTeams().getHome();
                    if (homeTeam != null) {
                        String translatedName = translationService.getTeamName(
                            homeTeam.getId(),
                            homeTeam.getName(),
                            languageCode
                        );
                        homeTeam.setDisplayName(translatedName);
                        log.debug("Set displayName for home team ID {}: {}", homeTeam.getId(), translatedName);
                    }

                    Fixture.Team awayTeam = fixture.getTeams().getAway();
                    if (awayTeam != null) {
                        String translatedName = translationService.getTeamName(
                            awayTeam.getId(),
                            awayTeam.getName(),
                            languageCode
                        );
                        awayTeam.setDisplayName(translatedName);
                        log.debug("Set displayName for away team ID {}: {}", awayTeam.getId(), translatedName);
                    }
                }
            });
        }
    }
}
