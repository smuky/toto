package com.muky.toto.service.ai;

import com.muky.toto.service.ApiFootballService;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GeminiAiService extends OpenAiServiceBase{

    private final OpenAiChatModel geminiChatModel;

    public GeminiAiService(@Qualifier("geminiChatModel") OpenAiChatModel chatModel, ApiFootballService apiFootballService) {
        super(apiFootballService);
        this.geminiChatModel = chatModel;
    }

    @Override
    protected OpenAiChatModel getChatModel() {
        return geminiChatModel;
    }

    @Override
    public String getPredictorId() {
        return "gemini";
    }
}
