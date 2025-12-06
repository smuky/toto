package com.muky.toto.controllers.response;

import com.muky.toto.model.LeagueEnum;
import com.muky.toto.model.TeamScoreEntry;

import java.util.List;
import java.util.Map;

public record AllTeamsResponse(
        List<TeamScoreEntry> teams,
        Map<LeagueEnum, String> leagueTranslations
) {
}
