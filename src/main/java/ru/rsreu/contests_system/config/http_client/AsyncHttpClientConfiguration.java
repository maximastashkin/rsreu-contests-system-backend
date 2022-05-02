package ru.rsreu.contests_system.config.http_client;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.Dsl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncHttpClientConfiguration {
    @Value("${code_executor_service.host}")
    private String codeExecutorServiceHost;

    @Value("${code_executor_service.port}")
    private String codeExecutorServicePort;

    @Value("${code_executor_service.execute_route}")
    private String codeExecutorServiceExecuteRoute;

    @Value("${code_executor_service.api_key}")
    private String codeExecutorServiceApiKey;

    @Bean
    public BoundRequestBuilder executorRequestBuilder() {
        AsyncHttpClient client = Dsl.asyncHttpClient();
        return client
                .prepareGet(formRequestUrl())
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", codeExecutorServiceApiKey);
    }

    @SuppressWarnings("HttpUrlsUsage")
    private String formRequestUrl() {
        return String.format("http://%s:%s%s",
                codeExecutorServiceHost, codeExecutorServicePort, codeExecutorServiceExecuteRoute);
    }
}
