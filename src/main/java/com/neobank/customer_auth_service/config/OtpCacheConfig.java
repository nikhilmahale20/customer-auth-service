package com.neobank.customer_auth_service.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class OtpCacheConfig {

    @Bean
    public Cache<String, String> otpCache() {

        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(3))
                .build();
    }
}