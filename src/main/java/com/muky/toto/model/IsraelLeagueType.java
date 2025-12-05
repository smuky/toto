package com.muky.toto.model;

import lombok.Getter;

@Getter
public enum IsraelLeagueType {
    NATIONAL_LEAGUE(LeagueEnum.ISRAEL_NATIONAL_LEAGUE),
    WINNER(LeagueEnum.ISRAEL_WINNER);

    private final LeagueEnum leagueEnum;

    IsraelLeagueType(LeagueEnum leagueEnum) {
        this.leagueEnum = leagueEnum;
    }
}
