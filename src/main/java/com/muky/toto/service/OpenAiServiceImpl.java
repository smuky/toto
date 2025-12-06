package com.muky.toto.service;

import com.muky.toto.model.Answer;
import com.muky.toto.model.LeagueEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
//@Service
@RequiredArgsConstructor
public class OpenAiServiceImpl implements OpenAiService {
    @Override
    public Answer getAnswer(String team1, String team2, String language, String extraInput, LeagueEnum leagueEnum) {
        return null;
    }
}
