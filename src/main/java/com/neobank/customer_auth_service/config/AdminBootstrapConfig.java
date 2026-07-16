package com.neobank.customer_auth_service.config;

import com.neobank.customer_auth_service.model.Credentials;
import com.neobank.customer_auth_service.model.Role;
import com.neobank.customer_auth_service.repository.CredentialsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminBootstrapConfig
        implements CommandLineRunner {

    private final CredentialsRepository credentialsRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(
            String... args
    ) {

        if (credentialsRepository.existsByRole(
                Role.ADMIN
        )) {

            log.info(
                    "Bootstrap ADMIN already exists"
            );

            return;
        }

        Credentials adminCredentials =
                Credentials.builder()
                        .customerId(null)
                        .username("admin")
                        .passwordHash(
                                passwordEncoder.encode(
                                        "Admin@123"
                                )
                        )
                        .role(Role.ADMIN)
                        .build();

        credentialsRepository.save(
                adminCredentials
        );

        log.info(
                "Bootstrap ADMIN account created successfully. Username: admin"
        );
    }
}