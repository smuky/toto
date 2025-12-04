package com.muky.toto.service;

import com.muky.toto.model.Answer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class EuropeCalculationService {
    private final OpenAiService openAiService;
    public Answer calculateAnswer(String homeTeam, String awayTeam) {
        Answer answer = openAiService.getEuropeLeagueAnswer(homeTeam, awayTeam);
        log.info("Question {}. /n Answer: {}", homeTeam + " - " + awayTeam, answer);
        return answer;
    }
}
