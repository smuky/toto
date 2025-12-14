package com.muky.toto.controllers;

import com.muky.toto.client.api_football.Fixture;
import com.muky.toto.client.api_football.League;
import com.muky.toto.client.api_football.Standing;
import com.muky.toto.model.LeagueEnum;
import com.muky.toto.model.apifootball.SupportedCountriesEnum;
import com.muky.toto.service.ApiFootballService;
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

    @Override
    public ResponseEntity<List<League>> getLeagues(SupportedCountriesEnum country) {
        log.info("Getting leagues for country: {}", country);
        List<League> leagues = apiFootballService.getLeagues(country);
        log.info("Retrieved {} leagues for country: {}", leagues.size(), country);
        return ResponseEntity.ok(leagues);
    }

    @Override
    public ResponseEntity<Standing> getStandings(LeagueEnum leagueEnum) {
        log.info("Getting Israel Premier League standings for league {}", leagueEnum.name());
        Standing standings = apiFootballService.getStanding(leagueEnum);
        log.info("Retrieved standings for league {} : {} ", leagueEnum.name(), standings);
        return ResponseEntity.ok(standings);
    }

    @Override
    public ResponseEntity<List<Fixture>> getNextFixtures(LeagueEnum leagueEnum, int next) {
        log.info("Getting next {} fixtures for league {}", next, leagueEnum.name());
        List<Fixture> fixtures = apiFootballService.getNextFixtures(leagueEnum, next);
        log.info("Retrieved {} fixtures for league {}", fixtures.size(), leagueEnum.name());
        return ResponseEntity.ok(fixtures);
    }
}
