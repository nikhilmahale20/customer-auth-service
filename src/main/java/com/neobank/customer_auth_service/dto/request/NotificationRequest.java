package com.neobank.customer_auth_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    private String recipient;

    private NotificationChannel channel;

    private String message;

    private String sourceEvent;
}