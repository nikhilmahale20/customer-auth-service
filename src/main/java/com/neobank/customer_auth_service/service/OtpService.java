package com.neobank.customer_auth_service.service;

public interface OtpService {

    void generateAndSendOtp(
            String customerId
    );

    void verifyOtp(
            String customerId,
            String otpCode
    );

    String generatePasswordResetOtp(
            String email
    );

    void verifyPasswordResetOtp(
            String customerId,
            String otpCode
    );

    boolean isPasswordResetVerified(
            String customerId
    );

    void clearPasswordResetVerification(
            String customerId
    );
}