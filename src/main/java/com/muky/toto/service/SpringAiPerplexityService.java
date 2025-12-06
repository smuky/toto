package com.muky.toto.service;

import com.muky.toto.cache.MemoryCache;
import com.muky.toto.model.Answer;
import com.muky.toto.model.LeagueEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service("springAiPerplexityService")
@RequiredArgsConstructor
public class SpringAiPerplexityService implements OpenAiService {
    
    private final OpenAiChatModel chatModel;
    private final MemoryCache memoryCache;

    @Value("classpath:/templates/TotoPredictionPrompt.st")
    private Resource totoPredictionTemplate;

    @Override
    public Answer getAnswer(String team1, String team2, String language, String extraInput, LeagueEnum leagueEnum) {
        PromptTemplate promptTemplate = new PromptTemplate(totoPredictionTemplate);
        Prompt prompt = promptTemplate.create(Map.of(
                "team1", team1,
                "team2", team2,
                "scoreBoard", memoryCache.getTeamsByLeague(leagueEnum),
                "language", language
        ));

        log.info("Prompt: {}", prompt);
        ChatResponse response = chatModel.call(prompt);

        return new Answer(response.getResult().getOutput().getText());
    }
}
