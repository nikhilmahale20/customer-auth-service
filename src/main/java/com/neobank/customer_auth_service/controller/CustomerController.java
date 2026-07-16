package com.neobank.customer_auth_service.controller;

import com.neobank.customer_auth_service.dto.request.CustomerRegisterRequest;
import com.neobank.customer_auth_service.dto.request.CustomerUpdateRequest;
import com.neobank.customer_auth_service.dto.request.CustomerStatusUpdateRequest;
import com.neobank.customer_auth_service.dto.response.ApiResponse;
import com.neobank.customer_auth_service.dto.response.CustomerRegisterResponse;
import com.neobank.customer_auth_service.dto.response.CustomerResponse;
import com.neobank.customer_auth_service.dto.request.UpdateKycStatusRequest;
import com.neobank.customer_auth_service.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(
        name = "Customer APIs",
        description = "Customer onboarding and profile management APIs"
)
public class CustomerController {

    private final CustomerService customerService;

    @Operation(
            summary = "Register customer",
            description = "Creates a new customer account and generates registration OTP"
    )
    @PostMapping(
            value = "/register",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<CustomerRegisterResponse>>
    registerCustomer(

            @Valid
            @RequestPart("request")
            CustomerRegisterRequest request,

            @RequestPart("panDocument")
            MultipartFile panDocument
    ) {

        CustomerRegisterResponse response =
                customerService.registerCustomer(
                        request,
                        panDocument
                );

        ApiResponse<CustomerRegisterResponse> apiResponse =
                ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        "Customer registered successfully. OTP generated for verification.",
                        response
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResponse);
    }

    @Operation(
            summary = "Get customer by ID",
            description = "Fetches customer details using customer ID"
    )
    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerResponse>>
    getCustomerById(
            @PathVariable String customerId
    ) {

        CustomerResponse response =
                customerService.getCustomerById(
                        customerId
                );

        ApiResponse<CustomerResponse> apiResponse =
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Customer fetched successfully",
                        response
                );

        return ResponseEntity.ok(apiResponse);
    }

    @Operation(
            summary = "Update customer profile",
            description = "Updates customer name, phone and address"
    )
    @PutMapping("/{customerId}/profile")
    public ResponseEntity<ApiResponse<CustomerResponse>>
    updateCustomerProfile(

            @PathVariable String customerId,

            @Valid
            @RequestBody CustomerUpdateRequest request
    ) {

        CustomerResponse response =
                customerService.updateCustomerProfile(
                        customerId,
                        request
                );

        ApiResponse<CustomerResponse> apiResponse =
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Customer profile updated successfully",
                        response
                );

        return ResponseEntity.ok(apiResponse);
    }

    @Operation(
            summary = "Update customer status",
            description = "Updates customer account status to ACTIVE or INACTIVE"
    )
    @PutMapping("/{customerId}/status")
    public ResponseEntity<ApiResponse<CustomerResponse>>
    updateCustomerStatus(

            @PathVariable String customerId,

            @Valid
            @RequestBody CustomerStatusUpdateRequest request
    ) {

        CustomerResponse response =
                customerService.updateCustomerStatus(
                        customerId,
                        request
                );

        ApiResponse<CustomerResponse> apiResponse =
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Customer status updated successfully",
                        response
                );

        return ResponseEntity.ok(
                apiResponse
        );
    }

    @Operation(
            summary = "Update customer KYC status",
            description = "Updates customer KYC status by customer ID"
    )
    @PutMapping("/{customerId}/kyc-status")
    public ResponseEntity<ApiResponse<CustomerResponse>>
    updateCustomerKycStatus(

            @PathVariable
            String customerId,

            @Valid
            @RequestBody
            UpdateKycStatusRequest request
    ) {

        CustomerResponse response =
                customerService
                        .updateCustomerKycStatus(
                                customerId,
                                request
                        );

        ApiResponse<CustomerResponse> apiResponse =
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Customer KYC status updated successfully",
                        response
                );

        return ResponseEntity.ok(
                apiResponse
        );
    }
}