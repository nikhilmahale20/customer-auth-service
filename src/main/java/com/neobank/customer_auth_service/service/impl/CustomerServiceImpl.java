package com.neobank.customer_auth_service.service.impl;

import com.neobank.customer_auth_service.dto.request.CustomerRegisterRequest;
import com.neobank.customer_auth_service.dto.request.CustomerStatusUpdateRequest;
import com.neobank.customer_auth_service.dto.request.CustomerUpdateRequest;

import com.neobank.customer_auth_service.dto.request.UpdateKycStatusRequest;
import com.neobank.customer_auth_service.dto.response.CustomerRegisterResponse;
import com.neobank.customer_auth_service.dto.response.CustomerResponse;

import com.neobank.customer_auth_service.event.CustomerRegisteredEvent;
import com.neobank.customer_auth_service.exception.DuplicateResourceException;
import com.neobank.customer_auth_service.exception.ResourceNotFoundException;
import com.neobank.customer_auth_service.producer.CustomerEventProducer;
import com.neobank.customer_auth_service.model.Credentials;
import com.neobank.customer_auth_service.model.Customer;
import com.neobank.customer_auth_service.model.CustomerStatus;
import com.neobank.customer_auth_service.model.KycStatus;
import com.neobank.customer_auth_service.model.Role;

import com.neobank.customer_auth_service.repository.CredentialsRepository;
import com.neobank.customer_auth_service.repository.CustomerRepository;

import com.neobank.customer_auth_service.service.CustomerService;
import com.neobank.customer_auth_service.service.OtpService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CredentialsRepository credentialsRepository;

    private final PasswordEncoder passwordEncoder;

    private final OtpService otpService;

    private final CustomerEventProducer customerEventProducer;

    @Override
    @Transactional
    public CustomerRegisterResponse registerCustomer(
            CustomerRegisterRequest request,
            MultipartFile panDocument
    ) {

        log.info(
                "Customer registration started for username: {}",
                request.username()
        );

        if (customerRepository.existsByEmail(request.email())) {

            throw new DuplicateResourceException(
                    "Email already exists"
            );
        }

        if (customerRepository.existsByPhone(request.phone())) {

            throw new DuplicateResourceException(
                    "Phone already exists"
            );
        }

        if (credentialsRepository.existsByUsername(
                request.username()
        )) {

            throw new DuplicateResourceException(
                    "Username already exists"
            );
        }

        byte[] panDocumentData;

        try {

            panDocumentData =
                    panDocument.getBytes();

        } catch (IOException exception) {

            throw new IllegalArgumentException(
                    "Unable to read PAN document"
            );
        }

        Customer customer = Customer.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .address(request.address())
                .panDocument(panDocumentData)
                .kycStatus(KycStatus.PENDING)
                .status(CustomerStatus.INACTIVE)
                .build();

        Customer savedCustomer =
                customerRepository.save(customer);

        Credentials credentials =
                Credentials.builder()
                        .customerId(
                                savedCustomer.getId()
                        )
                        .username(
                                request.username()
                        )
                        .passwordHash(
                                passwordEncoder.encode(
                                        request.password()
                                )
                        )
                        .role(Role.CUSTOMER)
                        .build();

        Credentials savedCredentials =
                credentialsRepository.save(
                        credentials
                );

        CustomerRegisteredEvent event =
                new CustomerRegisteredEvent(
                        UUID.randomUUID()
                                .toString(),
                        savedCustomer.getId(),
                        savedCustomer.getEmail(),
                        savedCustomer.getPhone(),
                        savedCustomer.getName(),
                        "CUSTOMER_REGISTERED",
                        LocalDateTime.now()
                                .withNano(0)
                                .toString()
                );

        customerEventProducer
                .publishCustomerRegisteredEvent(
                        event
                );
        otpService.generateAndSendOtp(
                savedCustomer.getId()
        );

        CustomerRegisterResponse response =
                new CustomerRegisterResponse(
                        savedCustomer.getId(),
                        savedCustomer.getName(),
                        savedCustomer.getEmail(),
                        savedCustomer.getPhone(),
                        savedCustomer.getAddress(),
                        savedCustomer.getKycStatus(),
                        savedCredentials.getUsername(),
                        savedCredentials.getRole()
                );

        log.info(
                "Customer registration completed successfully. Customer ID: {}",
                savedCustomer.getId()
        );

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(
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

        return mapToCustomerResponse(
                customer
        );
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomerProfile(
            String customerId,
            CustomerUpdateRequest request
    ) {

        Customer customer =
                customerRepository
                        .findById(customerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with ID: "
                                                + customerId
                                )
                        );

        if (!customer.getPhone()
                .equals(request.phone())
                && customerRepository.existsByPhone(
                request.phone()
        )) {

            throw new DuplicateResourceException(
                    "Phone number already registered"
            );
        }

        customer.setName(
                request.name()
        );

        customer.setPhone(
                request.phone()
        );

        customer.setAddress(
                request.address()
        );

        Customer updatedCustomer =
                customerRepository.save(
                        customer
                );

        log.info(
                "Customer profile updated successfully. Customer ID: {}",
                customerId
        );

        return mapToCustomerResponse(
                updatedCustomer
        );
    }

    private CustomerResponse mapToCustomerResponse(
            Customer customer
    ) {

        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getKycStatus(),
                customer.getStatus(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }


    @Transactional
    public CustomerResponse updateCustomerStatus(
            String customerId,
            CustomerStatusUpdateRequest request
    ) {

        Customer customer =
                customerRepository
                        .findById(customerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with ID: "
                                                + customerId
                                )
                        );

        if (customer.getStatus()
                == request.getStatus()) {

            throw new IllegalArgumentException(
                    "Customer status is already "
                            + request.getStatus()
            );
        }

        customer.setStatus(
                request.getStatus()
        );

        Customer updatedCustomer =
                customerRepository.save(
                        customer
                );

        log.info(
                "Customer status updated successfully. Customer ID: {}, Status: {}",
                customerId,
                updatedCustomer.getStatus()
        );

        return mapToCustomerResponse(
                updatedCustomer
        );
    }


    @Transactional
    public CustomerResponse updateCustomerKycStatus(
            String customerId,
            UpdateKycStatusRequest request
    ) {

        Customer customer =
                customerRepository
                        .findById(customerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with ID: "
                                                + customerId
                                )
                        );

        if (customer.getKycStatus()
                == request.kycStatus()) {

            throw new IllegalArgumentException(
                    "Customer KYC status is already "
                            + request.kycStatus()
            );
        }

        customer.setKycStatus(
                request.kycStatus()
        );

        Customer updatedCustomer =
                customerRepository.save(
                        customer
                );

        log.info(
                "Customer KYC status updated successfully. Customer ID: {}, KYC Status: {}",
                customerId,
                request.kycStatus()
        );

        return mapToCustomerResponse(
                updatedCustomer
        );
    }
}