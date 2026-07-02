package com.uti.svcreservations.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration for the two HTTP clients used to communicate with svc-rooms:
 * - RestTemplate: classic blocking client (availability checks, sync enrichment).
 * - WebClient: reactive client (enrichment on creation and by-email lookups).
 */
@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);
        return new RestTemplate(factory);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
