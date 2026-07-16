package com.neobank.customer_auth_service.dto.response;

import com.neobank.customer_auth_service.model.KycStatus;
import com.neobank.customer_auth_service.model.Role;

public record CustomerRegisterResponse(

        String id,
        String name,
        String email,
        String phone,
        String address,
        KycStatus kycStatus,
        String username,
        Role role

) {
}