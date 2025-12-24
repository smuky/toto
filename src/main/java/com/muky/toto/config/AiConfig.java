package com.muky.toto.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.URI;

@Slf4j
@Configuration
public class AiConfig {
    
    // Helper class to wrap HttpRequest with a modified URI
    private static class HttpRequestWrapper implements HttpRequest {
        private final HttpRequest delegate;
        private final URI uri;
        
        public HttpRequestWrapper(HttpRequest delegate, URI uri) {
            this.delegate = delegate;
            this.uri = uri;
        }
        
        @Override
        public URI getURI() {
            return uri;
        }
        
        @Override
        public org.springframework.http.HttpMethod getMethod() {
            return delegate.getMethod();
        }
        
        @Override
        public org.springframework.http.HttpHeaders getHeaders() {
            return delegate.getHeaders();
        }
        
        @Override
        public java.util.Map<String, Object> getAttributes() {
            return delegate.getAttributes();
        }
    }
    
    @Bean
    public OpenAiChatModel perplexityChatModel(
            @Value("${custom.ai.perplexity.api-key}") String apiKey,
            @Value("${custom.ai.perplexity.base-url}") String baseUrl,
            @Value("${custom.ai.perplexity.model}") String model,
            @Value("${custom.ai.perplexity.temperature}") double temperature) {

        log.info("Configuring Perplexity with base URL: {}", baseUrl);
        
        // Perplexity uses /chat/completions but Spring AI appends /v1/chat/completions
        // We intercept and rewrite the URL by replacing the path
        ClientHttpRequestInterceptor urlRewriteInterceptor = new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                URI originalUri = request.getURI();
                String originalUrl = originalUri.toString();
                
                // Replace /v1/chat/completions with /chat/completions
                if (originalUrl.contains("/v1/chat/completions")) {
                    String correctedUrl = originalUrl.replace("/v1/chat/completions", "/chat/completions");
                    log.info("Perplexity API - Rewriting URL from {} to {}", originalUrl, correctedUrl);
                    
                    URI correctedUri = URI.create(correctedUrl);
                    HttpRequest modifiedRequest = new HttpRequestWrapper(request, correctedUri);
                    return execution.execute(modifiedRequest, body);
                }
                
                log.info("Perplexity API Request URL: {}", originalUrl);
                return execution.execute(request, body);
            }
        };
        
        RestClient.Builder restClientBuilder = RestClient.builder()
                .requestInterceptor(urlRewriteInterceptor);

        OpenAiApi openAiApi = OpenAiApi.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .restClientBuilder(restClientBuilder)
                .build();

        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(model)
                        .temperature(temperature)
                        .build())
                .build();
    }

    @Bean
    public OpenAiChatModel geminiChatModel(
            @Value("${custom.ai.gemini.api-key}") String apiKey,
            @Value("${custom.ai.gemini.base-url}") String baseUrl,
            @Value("${custom.ai.gemini.model}") String modelName,
            @Value("${custom.ai.gemini.temperature}") double temperature) {

        log.info("Configuring Gemini (OpenAI Compat) with base URL: {}", baseUrl);

        // Reusing the URL rewriter logic to strip "/v1"
        ClientHttpRequestInterceptor urlRewriteInterceptor = (request, body, execution) -> {
            URI originalUri = request.getURI();
            String originalUrl = originalUri.toString();

            // Google's OpenAI endpoint is .../openai/chat/completions
            // Spring AI adds .../openai/v1/chat/completions
            // We must remove the "/v1" part
            if (originalUrl.contains("/v1/chat/completions")) {
                String correctedUrl = originalUrl.replace("/v1/chat/completions", "/chat/completions");
                log.info("Gemini API - Rewriting URL from {} to {}", originalUrl, correctedUrl);

                URI correctedUri = URI.create(correctedUrl);
                HttpRequest modifiedRequest = new HttpRequestWrapper(request, correctedUri);
                return execution.execute(modifiedRequest, body);
            }

            return execution.execute(request, body);
        };

        RestClient.Builder restClientBuilder = RestClient.builder()
                .requestInterceptor(urlRewriteInterceptor);

        OpenAiApi openAiApi = OpenAiApi.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .restClientBuilder(restClientBuilder)
                .build();

        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(modelName) // e.g., "gemini-1.5-flash"
                        .temperature(temperature) // Good for prediction consistency
                        .build())
                .build();
    }

    @Bean
    public OpenAiChatModel gptChatModel(
            @Value("${spring.ai.openai.api-key}") String apiKey,
            @Value("${spring.ai.openai.base-url:https://api.openai.com}") String baseUrl,
            @Value("${spring.ai.openai.chat.options.model:gpt-4o}") String model,
            @Value("${spring.ai.openai.chat.options.temperature:0.2}") double temperature
            ) {

        // 1. Create the API Connection
        OpenAiApi openAiApi = OpenAiApi.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .build();

        // 2. Create the Chat Model with Options
        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(model)
                        .temperature(temperature)
                        .build())
                .build();
    }
}
