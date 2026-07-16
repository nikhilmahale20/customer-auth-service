package com.neobank.customer_auth_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordRequest {

    @NotBlank(
            message = "Customer ID is required"
    )
    private String customerId;

    @NotBlank(
            message = "New password is required"
    )
    @Size(
            min = 8,
            message = "Password must contain at least 8 characters"
    )
    private String newPassword;
}