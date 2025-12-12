package com.muky.toto.service;

import com.muky.toto.ai_response.TodoPredictionPromptResponse;
import com.muky.toto.cache.MemoryCache;
import com.muky.toto.model.Answer;
import com.muky.toto.model.LeagueEnum;
import com.muky.toto.model.TeamScoreEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service("springAiPerplexityService")
@RequiredArgsConstructor
public class SpringAiPerplexityService implements OpenAiService {
    
    private final OpenAiChatModel chatModel;
    private final MemoryCache memoryCache;

    @Value("classpath:/templates/TotoPredictionPrompt.st")
    private Resource totoPredictionTemplate;

    @Value("classpath:/templates/TotoPredictionPromptHebrew.st")
    private Resource totoPredictionTemplateHebrew;

    @Override
    public Answer getAnswer(String team1, String team2, String language, String extraInput, LeagueEnum leagueEnum) {
        PromptTemplate promptTemplate = new PromptTemplate(totoPredictionTemplateHebrew);
        List<TeamScoreEntry> scoreBoard = memoryCache.getTeamsByLeague(leagueEnum);
        Prompt prompt = promptTemplate.create(Map.of(
                "team1", team1,
                "team2", team2,
                "scoreBoard", scoreBoard,
                "language", language,
                "leagueLanguage", leagueEnum.getLeagueLanguage()
        ));

        log.info("Prompt: {}", prompt);
        ChatResponse response = chatModel.call(prompt);

        return new Answer(response.getResult().getOutput().getText());
    }

    @Override
    public TodoPredictionPromptResponse getTodoPredictionPromptResponse(String team1, String team2, String language, String extraInput, LeagueEnum leagueEnum) {
        // 1. Create the converter with your class
        BeanOutputConverter<TodoPredictionPromptResponse> converter = new BeanOutputConverter<>(TodoPredictionPromptResponse.class);

        // 2. Get the schema instructions (This string contains: "Your output must be JSON...")
        String formatInstructions = converter.getFormat();

        // 3. Update your PromptTemplate to include the {format} placeholder
        // Note: I added {format} at the end of the template string below
        PromptTemplate promptTemplate = new PromptTemplate(totoPredictionTemplate);
        List<TeamScoreEntry> scoreBoard = memoryCache.getTeamsByLeague(leagueEnum);
        Prompt prompt = promptTemplate.create(Map.of(
                "team1", team1,
                "team2", team2,
                "scoreBoard", scoreBoard,
                "language", language,
                "leagueLanguage", leagueEnum.getLeagueLanguage(),
                "format", formatInstructions // <--- Pass the instructions here
        ));

        log.info("Prompt: {}", prompt);

        // 4. Call the model with temperature
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .temperature(0.3)
                .build();
        
        Prompt promptWithOptions = new Prompt(prompt.getInstructions(), options);
        ChatResponse response = chatModel.call(promptWithOptions);

        // 5. Convert the raw string response into your Java Object
        TodoPredictionPromptResponse prediction = converter.convert(response.getResult().getOutput().getText());

        // Now you have a typed object!
        log.info("Home Win Probability: {}%", prediction.probabilities().get("1"));
        log.info("Justification (in {}): {}", language, prediction.justification());

        return prediction;
    }
}
