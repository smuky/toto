package com.muky.toto.service.ai;

import com.muky.toto.ai_response.TodoPredictionPromptResponse;
import com.muky.toto.client.api_football.Standing;
import com.muky.toto.client.api_football.prediction.MatchAnalysisData;
import com.muky.toto.model.LeagueEnum;
import com.muky.toto.model.SupportedLanguageEnum;
import com.muky.toto.service.ApiFootballService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.util.Map;

@Slf4j
public abstract class OpenAiServiceBase implements OpenAiService{

    private final ApiFootballService apiFootballService;

    @Value("classpath:/templates/TotoPredictionPrompt.st")
    private Resource totoPredictionTemplate;

    @Value("classpath:/templates/CleanMatchPredictionPrompt.st")
    private Resource cleanMatchPredictionTemplate;

    protected OpenAiServiceBase(ApiFootballService apiFootballService) {
        this.apiFootballService = apiFootballService;
    }

    @Override
    public TodoPredictionPromptResponse getTodoPredictionPromptResponse(String team1, String team2, String language, String extraInput, LeagueEnum leagueEnum) {
        // 1. Create the converter with your class
        BeanOutputConverter<TodoPredictionPromptResponse> converter = new BeanOutputConverter<>(TodoPredictionPromptResponse.class);

        // 2. Get the schema instructions (This string contains: "Your output must be JSON...")
        String formatInstructions = converter.getFormat();
        Standing standing = apiFootballService.getStanding(leagueEnum);

        // 3. Update your PromptTemplate to include the {format} placeholder
        // Note: I added {format} at the end of the template string below
        PromptTemplate promptTemplate = new PromptTemplate(totoPredictionTemplate);
        Prompt prompt = promptTemplate.create(Map.of(
                "team1", team1,
                "team2", team2,
                "scoreBoard", standing,
                "language", language,
                "leagueLanguage", leagueEnum.getLeagueLanguage(),
                "format", formatInstructions // <--- Pass the instructions here
        ));

        log.info("Prompt: {}", prompt);

        // 4. Call the model with temperature and max tokens
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .temperature(0.3)
                .maxTokens(4000)
                .build();

        Prompt promptWithOptions = new Prompt(prompt.getInstructions(), options);

        ChatResponse response = getChatModel().call(promptWithOptions);

        // 5. Convert the raw string response into your Java Object
        String rawResponse = response.getResult().getOutput().getText();
        String finishReason = response.getResult().getMetadata().getFinishReason();

        log.debug("Raw AI response: {}", rawResponse);
        log.debug("AI finish reason: {}", finishReason);

        // Check if response was truncated
        if ("length".equals(finishReason)) {
            log.error("AI response was truncated due to token limit (maxTokens=4000). Response length: {} chars", rawResponse.length());
            throw new RuntimeException("AI response was truncated. Please simplify the prompt or increase maxTokens.");
        }

        try {
            TodoPredictionPromptResponse prediction = converter.convert(rawResponse);
            log.info("Justification (in {}): {}", language, prediction.justification());
            return prediction;
        } catch (Exception e) {
            log.error("Failed to parse AI response. Raw response: {}", rawResponse, e);
            throw new RuntimeException("Failed to parse AI response - response may be malformed", e);
        }
    }

    protected abstract OpenAiChatModel getChatModel();

    @Override
    public TodoPredictionPromptResponse getCleanMatchPrediction(String team1, String team2, MatchAnalysisData matchData, String language) {
        BeanOutputConverter<TodoPredictionPromptResponse> converter = new BeanOutputConverter<>(TodoPredictionPromptResponse.class);
        String formatInstructions = converter.getFormat();
        SupportedLanguageEnum supportedLanguageEnum = SupportedLanguageEnum.valueOf(language.toUpperCase());

        PromptTemplate promptTemplate = new PromptTemplate(cleanMatchPredictionTemplate);
        Prompt prompt = promptTemplate.create(Map.of(
                "team1", team1,
                "team2", team2,
                "matchData", matchData,
                "language", supportedLanguageEnum.getLanguage(),
                "format", formatInstructions
        ));

        log.info("Calling AI with clean MatchAnalysisData for teams: {} vs {} in language: {}",
                matchData.getHomeTeam().getName(),
                matchData.getAwayTeam().getName(),
                language);

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .temperature(0.3)
                .maxTokens(4000)
                .build();

        Prompt promptWithOptions = new Prompt(prompt.getInstructions(), options);
        ChatResponse response = getChatModel().call(promptWithOptions);

        String rawResponse = response.getResult().getOutput().getText();
        String finishReason = response.getResult().getMetadata().getFinishReason();

        log.debug("Raw AI response: {}", rawResponse);
        log.debug("AI finish reason: {}", finishReason);

        if ("length".equals(finishReason)) {
            log.error("AI response was truncated due to token limit (maxTokens=3500). Response length: {} chars", rawResponse.length());
            throw new RuntimeException("AI response was truncated. Please simplify the prompt or increase maxTokens.");
        }

        try {
            TodoPredictionPromptResponse prediction = converter.convert(rawResponse);
            log.info("Generated match prediction using clean objective data in {}", language);
            return prediction;
        } catch (Exception e) {
            log.error("Failed to parse AI response. Raw response: {}", rawResponse, e);
            throw new RuntimeException("Failed to parse AI response - response may be malformed", e);
        }
    }
}
