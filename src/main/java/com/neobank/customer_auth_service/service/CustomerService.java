package com.neobank.customer_auth_service.service;

import com.neobank.customer_auth_service.dto.request.CustomerRegisterRequest;
import com.neobank.customer_auth_service.dto.request.CustomerStatusUpdateRequest;
import com.neobank.customer_auth_service.dto.request.CustomerUpdateRequest;
import com.neobank.customer_auth_service.dto.request.UpdateKycStatusRequest;
import com.neobank.customer_auth_service.dto.response.CustomerRegisterResponse;
import com.neobank.customer_auth_service.dto.response.CustomerResponse;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface CustomerService {

    CustomerRegisterResponse registerCustomer(
            CustomerRegisterRequest request,
            MultipartFile panDocument
    );

    CustomerResponse getCustomerById(
            String customerId
    );

    CustomerResponse updateCustomerProfile(
            String customerId,
            CustomerUpdateRequest request
    );

    CustomerResponse updateCustomerStatus(
            String customerId,
            CustomerStatusUpdateRequest request
    );

    CustomerResponse updateCustomerKycStatus(
            String customerId,
            UpdateKycStatusRequest request
    );


}