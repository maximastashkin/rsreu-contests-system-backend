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

    @Value("${code_executor_service.alive_route}")
    private String codeExecutorAliveRoute;

    @Value("${code_executor_service.api_key}")
    private String codeExecutorServiceApiKey;

    @Bean("executorRequestBuilder")
    public BoundRequestBuilder executorRequestBuilder() {
        AsyncHttpClient client = Dsl.asyncHttpClient();
        return client
                .prepareGet(formRequestUrl(codeExecutorServiceExecuteRoute))
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", codeExecutorServiceApiKey);
    }

    @Bean("aliveRequestBuilder")
    public BoundRequestBuilder aliveRequestBuilder() {
        AsyncHttpClient client = Dsl.asyncHttpClient();
        return client
                .prepareGet(formRequestUrl(codeExecutorAliveRoute))
                .addHeader("x-api-key", codeExecutorServiceApiKey);
    }

    @SuppressWarnings("HttpUrlsUsage")
    private String formRequestUrl(String route) {
        return String.format("http://%s:%s/api%s",
                codeExecutorServiceHost, codeExecutorServicePort, route);
    }
}
