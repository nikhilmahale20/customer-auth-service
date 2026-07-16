package com.neobank.customer_auth_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VerifyOtpRequest(

        @NotBlank(message = "Customer ID is required")
        String customerId,

        @NotBlank(message = "OTP is required")
        @Pattern(
                regexp = "\\d{6}",
                message = "OTP must be a 6-digit number"
        )
        String otpCode

) {
}