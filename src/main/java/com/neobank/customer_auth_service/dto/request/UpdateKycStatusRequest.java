package com.neobank.customer_auth_service.dto.request;

import jakarta.validation.constraints.NotNull;

import com.neobank.customer_auth_service.model.KycStatus;

public record UpdateKycStatusRequest(

        @NotNull(
                message = "KYC status is required"
        )
        KycStatus kycStatus

) {
}