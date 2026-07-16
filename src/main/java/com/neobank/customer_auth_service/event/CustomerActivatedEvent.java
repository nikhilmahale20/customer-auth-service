package com.neobank.customer_auth_service.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerActivatedEvent {

    private String eventId;

    private String customerId;

    private String email;

    private String mobileNumber;

    private String firstName;

    private String eventType;

    private String occurredAt;
}