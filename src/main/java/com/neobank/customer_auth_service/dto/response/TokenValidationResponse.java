package com.neobank.customer_auth_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidationResponse {

    private boolean valid;

    private String username;

    private String customerId;

    private String role;
}