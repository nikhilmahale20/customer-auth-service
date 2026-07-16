package com.neobank.customer_auth_service.dto.response;

public record LoginResponse(

        String token,
        String tokenType

) {
}