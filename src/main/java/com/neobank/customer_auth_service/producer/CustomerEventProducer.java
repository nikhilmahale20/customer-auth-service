package com.neobank.customer_auth_service.producer;

import com.neobank.customer_auth_service.event.CustomerActivatedEvent;
import com.neobank.customer_auth_service.event.CustomerRegisteredEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerEventProducer {

    private static final String CUSTOMER_EVENTS_TOPIC =
            "customer-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishCustomerRegisteredEvent(
            CustomerRegisteredEvent event
    ) {

        kafkaTemplate.send(
                CUSTOMER_EVENTS_TOPIC,
                event.getCustomerId(),
                event
        );

        log.info(
                "CustomerRegisteredEvent published. Event ID: {}, Customer ID: {}",
                event.getEventId(),
                event.getCustomerId()
        );
    }

    public void publishCustomerActivatedEvent(
            CustomerActivatedEvent event
    ) {

        kafkaTemplate.send(
                CUSTOMER_EVENTS_TOPIC,
                event.getCustomerId(),
                event
        );

        log.info(
                "CustomerActivatedEvent published. Event ID: {}, Customer ID: {}",
                event.getEventId(),
                event.getCustomerId()
        );
    }
}