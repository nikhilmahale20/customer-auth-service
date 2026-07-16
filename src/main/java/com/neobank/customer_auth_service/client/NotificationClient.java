package com.neobank.customer_auth_service.client;

import com.neobank.customer_auth_service.dto.request.NotificationRequest;
import com.neobank.customer_auth_service.dto.response.NotificationResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class NotificationClient {

    private final RestClient.Builder restClientBuilder;

    public NotificationResponse sendNotification(
            NotificationRequest request
    ) {

        return restClientBuilder
                .build()
                .post()
                .uri(
                        "http://notification-service"
                                + "/api/v1/notifications/send"
                )
                .body(request)
                .retrieve()
                .body(NotificationResponse.class);
    }
}