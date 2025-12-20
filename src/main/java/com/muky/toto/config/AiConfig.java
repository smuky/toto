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
            @Value("${custom.ai.perplexity.base-url}") String baseUrl) {

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
                        .model("sonar")
                        .build())
                .build();
    }
}
