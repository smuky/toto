package com.muky.toto.service.ai;

import com.muky.toto.service.ApiFootballService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SpringAiPerplexityService extends OpenAiServiceBase{
    private final OpenAiChatModel chatModel;

    public SpringAiPerplexityService(@Qualifier("perplexityChatModel") OpenAiChatModel chatModel, ApiFootballService apiFootballService) {
        super(apiFootballService);
        this.chatModel = chatModel;
    }

    @Override
    public String getPredictorId() {
        return "perplexity";
    }

    @Override
    protected OpenAiChatModel getChatModel() {
        return chatModel;
    }
}
