package com.muky.toto.controllers;

import com.muky.toto.client.api_football.League;
import com.muky.toto.client.api_football.Standing;
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
    public ResponseEntity<List<Standing>> getIsraelPremierLeagueStandings(int season) {
        log.info("Getting Israel Premier League standings for season {}", season);
        List<Standing> standings = apiFootballService.getIsraelPremierLeagueStandings(season);
        log.info("Retrieved {} standings", standings.size());
        return ResponseEntity.ok(standings);
    }
}
