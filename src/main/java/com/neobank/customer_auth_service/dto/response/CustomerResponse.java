package com.neobank.customer_auth_service.dto.response;

import com.neobank.customer_auth_service.model.CustomerStatus;
import com.neobank.customer_auth_service.model.KycStatus;

import java.time.OffsetDateTime;

public record CustomerResponse(

        String id,

        String name,

        String email,

        String phone,

        String address,

        KycStatus kycStatus,

        CustomerStatus status,

        OffsetDateTime createdAt,

        OffsetDateTime updatedAt

) {
}