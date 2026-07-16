package com.neobank.customer_auth_service.repository;

import com.neobank.customer_auth_service.model.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository
        extends JpaRepository<Customer, String> {

    boolean existsByEmail(
            String email
    );

    boolean existsByPhone(
            String phone
    );

    Optional<Customer> findByEmail(
            String email
    );
}