package com.muky.toto.service;

import com.muky.toto.model.Answer;
import com.muky.toto.model.TeamGamesEntry;
import com.muky.toto.model.TeamScoreEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiServiceImpl implements OpenAiService {
    private final ChatModel chatModel;

    @Value("classpath:/templates/israelNationalLeaguePromptMain.st")
    private Resource ragPromptTemplate;

    @Value("classpath:/templates/EuropeLeaguePredictorPrompt.st")
    private Resource europeLeagueTemplate;

    public Answer getEuropeLeagueAnswer(String homeTeam, String awayTeam) {
        PromptTemplate promptTemplate = new PromptTemplate(europeLeagueTemplate);
        Prompt prompt = promptTemplate.create(Map.of(
                "homeTeam", homeTeam,
                "awayTeam", awayTeam
        ));

        log.info("Prompt: {}", prompt);
        ChatResponse response = chatModel.call(prompt);

        return new Answer(response.getResult().getOutput().getText());


    }
    @Override
    public Answer getAnswer(String leagueName, String homeTeam, String awayTeam,
                            String extraInput, List<TeamScoreEntry> scoreBoard,
                            List<TeamGamesEntry> homeTeamGames, List<TeamGamesEntry> awayTeamGames) {
        
        // Convert lists to formatted strings
        String scoreBoardStr = scoreBoard.stream()
                .map(TeamScoreEntry::toString)
                .collect(Collectors.joining("\n"));
        
        String homeTeamGamesStr = homeTeamGames.stream()
                .map(TeamGamesEntry::toString)
                .collect(Collectors.joining("\n"));
        
        String awayTeamGamesStr = awayTeamGames.stream()
                .map(TeamGamesEntry::toString)
                .collect(Collectors.joining("\n"));
        
        // Create the prompt with all parameters
        PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
        Prompt prompt = promptTemplate.create(Map.of(
                "leagueName", leagueName,
                "homeTeam", homeTeam,
                "awayTeam", awayTeam,
                "extraInput", extraInput != null ? extraInput : "",
                "scoreBoard", scoreBoardStr,
                "homeTeamGames", homeTeamGamesStr,
                "awayTeamGames", awayTeamGamesStr
        ));

        log.info("Prompt: {}", prompt);
        ChatResponse response = chatModel.call(prompt);

        return new Answer(response.getResult().getOutput().getText());
    }
}
