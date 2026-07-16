package com.neobank.customer_auth_service.repository;

import com.neobank.customer_auth_service.model.Credentials;
import com.neobank.customer_auth_service.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialsRepository
        extends JpaRepository<Credentials, String> {

    boolean existsByUsername(
            String username
    );

    boolean existsByRole(
            Role role
    );

    Optional<Credentials> findByUsername(
            String username
    );

    Optional<Credentials> findByCustomerId(
            String customerId
    );
}