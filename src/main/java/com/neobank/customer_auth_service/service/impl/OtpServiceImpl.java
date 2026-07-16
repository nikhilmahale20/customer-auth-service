package com.neobank.customer_auth_service.service.impl;

import com.github.benmanes.caffeine.cache.Cache;

import com.neobank.customer_auth_service.client.NotificationClient;

import com.neobank.customer_auth_service.dto.request.NotificationChannel;
import com.neobank.customer_auth_service.dto.request.NotificationRequest;

import com.neobank.customer_auth_service.dto.response.NotificationResponse;

import com.neobank.customer_auth_service.exception.ResourceNotFoundException;

import com.neobank.customer_auth_service.model.Customer;
import com.neobank.customer_auth_service.model.CustomerStatus;

import com.neobank.customer_auth_service.repository.CustomerRepository;

import com.neobank.customer_auth_service.service.OtpService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import com.neobank.customer_auth_service.event.CustomerActivatedEvent;
import com.neobank.customer_auth_service.producer.CustomerEventProducer;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {

    private static final String REGISTRATION_OTP_PREFIX =
            "REGISTRATION_OTP:";

    private static final String PASSWORD_RESET_OTP_PREFIX =
            "PASSWORD_RESET_OTP:";

    private static final String PASSWORD_RESET_VERIFIED_PREFIX =
            "PASSWORD_RESET_VERIFIED:";

    private final CustomerRepository customerRepository;

    private final NotificationClient notificationClient;

    private final Cache<String, String> otpCache;

    private final CustomerEventProducer customerEventProducer;

    private final SecureRandom secureRandom =
            new SecureRandom();

    @Override
    public void generateAndSendOtp(
            String customerId
    ) {

        Customer customer =
                customerRepository
                        .findById(customerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found"
                                )
                        );

        if (customer.getStatus()
                == CustomerStatus.ACTIVE) {

            throw new IllegalArgumentException(
                    "Customer is already active"
            );
        }

        String otpCode =
                generateOtp();

        String cacheKey =
                REGISTRATION_OTP_PREFIX
                        + customerId;

        otpCache.put(
                cacheKey,
                otpCode
        );

        log.info(
                "Registration OTP generated. Customer ID: {}, Email: {}, OTP: {}",
                customerId,
                customer.getEmail(),
                otpCode
        );

        String message =
                "Your NeoBank verification OTP is "
                        + otpCode
                        + ". It is valid for 3 minutes.";

        NotificationRequest notificationRequest =
                new NotificationRequest(
                        customer.getEmail(),
                        NotificationChannel.EMAIL,
                        message,
                        "CUSTOMER_REGISTRATION_OTP"
                );

        NotificationResponse notificationResponse =
                notificationClient.sendNotification(
                        notificationRequest
                );

        log.info(
                "Registration OTP notification sent. Customer ID: {}, Notification ID: {}, Status: {}",
                customerId,
                notificationResponse.getId(),
                notificationResponse.getStatus()
        );
    }

    @Override
    @Transactional
    public void verifyOtp(
            String customerId,
            String otpCode
    ) {

        Customer customer =
                customerRepository
                        .findById(customerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found"
                                )
                        );

        if (customer.getStatus()
                == CustomerStatus.ACTIVE) {

            throw new IllegalArgumentException(
                    "Customer is already active"
            );
        }

        String cacheKey =
                REGISTRATION_OTP_PREFIX
                        + customerId;

        String cachedOtp =
                otpCache.getIfPresent(
                        cacheKey
                );

        if (cachedOtp == null) {

            throw new IllegalArgumentException(
                    "OTP is invalid or expired"
            );
        }

        if (!cachedOtp.equals(otpCode)) {

            throw new IllegalArgumentException(
                    "Invalid OTP"
            );
        }

        customer.setStatus(
                CustomerStatus.ACTIVE
        );

        customerRepository.save(
                customer
        );

        CustomerActivatedEvent event =
                new CustomerActivatedEvent(
                        UUID.randomUUID().toString(),
                        customer.getId(),
                        customer.getEmail(),
                        customer.getPhone(),
                        customer.getName(),
                        "CUSTOMER_ACTIVATED",
                        LocalDateTime.now()
                                .withNano(0)
                                .toString()
                );

        customerEventProducer
                .publishCustomerActivatedEvent(
                        event
                );

        otpCache.invalidate(
                cacheKey
        );

        log.info(
                "Registration OTP verified. Customer activated. Customer ID: {}",
                customerId
        );
    }

    @Override
    public String generatePasswordResetOtp(
            String email
    ) {

        Customer customer =
                customerRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with email: "
                                                + email
                                )
                        );

        if (customer.getStatus()
                != CustomerStatus.ACTIVE) {

            throw new IllegalArgumentException(
                    "Customer account is not active"
            );
        }

        String otpCode =
                generateOtp();

        String otpCacheKey =
                PASSWORD_RESET_OTP_PREFIX
                        + customer.getId();

        String verifiedCacheKey =
                PASSWORD_RESET_VERIFIED_PREFIX
                        + customer.getId();

        otpCache.invalidate(
                verifiedCacheKey
        );

        otpCache.put(
                otpCacheKey,
                otpCode
        );

        String message =
                "Your NeoBank password reset OTP is "
                        + otpCode
                        + ". It is valid for 3 minutes.";

        NotificationRequest notificationRequest =
                new NotificationRequest(
                        customer.getEmail(),
                        NotificationChannel.EMAIL,
                        message,
                        "CUSTOMER_PASSWORD_RESET_OTP"
                );

        NotificationResponse notificationResponse =
                notificationClient.sendNotification(
                        notificationRequest
                );

        log.info(
                "Password reset OTP notification sent. Customer ID: {}, Notification ID: {}, Status: {}",
                customer.getId(),
                notificationResponse.getId(),
                notificationResponse.getStatus()
        );

        return customer.getId();
    }

    @Override
    public void verifyPasswordResetOtp(
            String customerId,
            String otpCode
    ) {

        customerRepository
                .findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found"
                        )
                );

        String otpCacheKey =
                PASSWORD_RESET_OTP_PREFIX
                        + customerId;

        String cachedOtp =
                otpCache.getIfPresent(
                        otpCacheKey
                );

        if (cachedOtp == null) {

            throw new IllegalArgumentException(
                    "Password reset OTP is invalid or expired"
            );
        }

        if (!cachedOtp.equals(otpCode)) {

            throw new IllegalArgumentException(
                    "Invalid password reset OTP"
            );
        }

        otpCache.invalidate(
                otpCacheKey
        );

        String verifiedCacheKey =
                PASSWORD_RESET_VERIFIED_PREFIX
                        + customerId;

        otpCache.put(
                verifiedCacheKey,
                "VERIFIED"
        );

        log.info(
                "Password reset OTP verified successfully. Customer ID: {}",
                customerId
        );
    }

    @Override
    public boolean isPasswordResetVerified(
            String customerId
    ) {

        String verifiedCacheKey =
                PASSWORD_RESET_VERIFIED_PREFIX
                        + customerId;

        String verificationStatus =
                otpCache.getIfPresent(
                        verifiedCacheKey
                );

        return "VERIFIED".equals(
                verificationStatus
        );
    }

    @Override
    public void clearPasswordResetVerification(
            String customerId
    ) {

        String verifiedCacheKey =
                PASSWORD_RESET_VERIFIED_PREFIX
                        + customerId;

        otpCache.invalidate(
                verifiedCacheKey
        );

        log.info(
                "Password reset verification cleared. Customer ID: {}",
                customerId
        );
    }

    private String generateOtp() {

        return String.format(
                "%06d",
                secureRandom.nextInt(
                        1_000_000
                )
        );
    }
}