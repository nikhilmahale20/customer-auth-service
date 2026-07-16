package com.neobank.customer_auth_service.config;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilder() {

        return RestClient.builder();
    }

    @Bean
    public RestClient notificationRestClient(
            RestClient.Builder loadBalancedRestClientBuilder,
            @Value("${notification.service.base-url}")
            String notificationServiceBaseUrl
    ) {

        return loadBalancedRestClientBuilder
                .baseUrl(notificationServiceBaseUrl)
                .build();
    }
}