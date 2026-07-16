package com.neobank.customer_auth_service.service.impl;

import com.neobank.customer_auth_service.dto.request.ChangePasswordRequest;
import com.neobank.customer_auth_service.dto.request.LoginRequest;
import com.neobank.customer_auth_service.dto.request.ResetPasswordRequest;

import com.neobank.customer_auth_service.dto.response.LoginResponse;

import com.neobank.customer_auth_service.exception.ResourceNotFoundException;

import com.neobank.customer_auth_service.model.Credentials;

import com.neobank.customer_auth_service.model.Customer;
import com.neobank.customer_auth_service.model.CustomerStatus;
import com.neobank.customer_auth_service.model.Role;
import com.neobank.customer_auth_service.repository.CredentialsRepository;

import com.neobank.customer_auth_service.repository.CustomerRepository;
import com.neobank.customer_auth_service.service.AuthService;
import com.neobank.customer_auth_service.service.JwtService;
import com.neobank.customer_auth_service.service.OtpService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final CredentialsRepository credentialsRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    private final CustomerRepository customerRepository;

    private final OtpService otpService;

    @Override
    public LoginResponse login(
            LoginRequest request
    ) {

        Credentials credentials =
                credentialsRepository
                        .findByUsername(
                                request.username()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid username or password"
                                )
                        );

        if (!passwordEncoder.matches(
                request.password(),
                credentials.getPasswordHash()
        )) {

            throw new IllegalArgumentException(
                    "Invalid username or password"
            );
        }

        if (credentials.getRole()
                == Role.CUSTOMER) {

            Customer customer =
                    customerRepository
                            .findById(
                                    credentials.getCustomerId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Customer not found"
                                    )
                            );

            if (customer.getStatus()
                    != CustomerStatus.ACTIVE) {

                throw new IllegalArgumentException(
                        "Customer account is inactive"
                );
            }
        }

        String token =
                jwtService.generateToken(
                        credentials
                );

        log.info(
                "Login successful. Username: {}, Role: {}",
                credentials.getUsername(),
                credentials.getRole()
        );

        return new LoginResponse(
                token,
                "Bearer"
        );
    }

    @Override
    @Transactional
    public void changePassword(
            String customerId,
            ChangePasswordRequest request
    ) {

        Credentials credentials =
                credentialsRepository
                        .findByCustomerId(
                                customerId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer credentials not found"
                                )
                        );

        if (!passwordEncoder.matches(
                request.currentPassword(),
                credentials.getPasswordHash()
        )) {

            throw new IllegalArgumentException(
                    "Current password is incorrect"
            );
        }

        if (passwordEncoder.matches(
                request.newPassword(),
                credentials.getPasswordHash()
        )) {

            throw new IllegalArgumentException(
                    "New password must be different from current password"
            );
        }

        credentials.setPasswordHash(
                passwordEncoder.encode(
                        request.newPassword()
                )
        );

        credentialsRepository.save(
                credentials
        );

        log.info(
                "Password changed successfully. Customer ID: {}",
                customerId
        );
    }

    @Override
    @Transactional
    public void resetPassword(
            ResetPasswordRequest request
    ) {

        String customerId =
                request.getCustomerId();

        if (!otpService.isPasswordResetVerified(
                customerId
        )) {

            throw new IllegalArgumentException(
                    "Password reset OTP verification is required"
            );
        }

        Credentials credentials =
                credentialsRepository
                        .findByCustomerId(
                                customerId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer credentials not found"
                                )
                        );

        if (passwordEncoder.matches(
                request.getNewPassword(),
                credentials.getPasswordHash()
        )) {

            throw new IllegalArgumentException(
                    "New password must be different from current password"
            );
        }

        credentials.setPasswordHash(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        credentialsRepository.save(
                credentials
        );

        otpService.clearPasswordResetVerification(
                customerId
        );

        log.info(
                "Password reset successfully. Customer ID: {}",
                customerId
        );
    }
}