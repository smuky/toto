package com.muky.toto.model;

import lombok.Getter;

@Getter
public enum IsraelLeagueType {
    NATIONAL_LEAGUE("FolderID=80", LeagueEnum.ISRAEL_NATIONAL_LEAGUE),
    WINNER("FolderID=44", LeagueEnum.ISRAEL_WINNER);

    private final LeagueEnum leagueEnum;
    private final String sport5ClientSuffix;

    IsraelLeagueType(String sport5ClientSuffix, LeagueEnum leagueEnum) {
        this.sport5ClientSuffix = sport5ClientSuffix;
        this.leagueEnum = leagueEnum;
    }

    public String getSuffix() {
        return sport5ClientSuffix;
    }
}
