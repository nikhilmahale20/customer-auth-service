package com.neobank.customer_auth_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VerifyResetOtpRequest {

    @NotBlank(
            message = "Customer ID is required"
    )
    private String customerId;

    @NotBlank(
            message = "OTP code is required"
    )
    @Pattern(
            regexp = "^[0-9]{6}$",
            message = "OTP must contain exactly 6 digits"
    )
    private String otpCode;
}