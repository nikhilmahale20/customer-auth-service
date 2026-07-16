package com.neobank.customer_auth_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerRegisterRequest(

        @NotBlank(message = "Name is required")
        @Size(
                min = 2,
                max = 100,
                message = "Name must be between 2 and 100 characters"
        )
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(
                max = 255,
                message = "Email must not exceed 255 characters"
        )
        String email,

        @NotBlank(message = "Phone is required")
        @Pattern(
                regexp = "^[6-9][0-9]{9}$",
                message = "Phone number must be a valid 10-digit Indian mobile number"
        )
        String phone,

        @NotBlank(message = "Address is required")
        @Size(
                min = 5,
                max = 500,
                message = "Address must be between 5 and 500 characters"
        )
        String address,

        @NotBlank(message = "Username is required")
        @Size(
                min = 4,
                max = 100,
                message = "Username must be between 4 and 100 characters"
        )
        String username,

        @NotBlank(message = "Password is required")
        @Size(
                min = 8,
                max = 72,
                message = "Password must be between 8 and 72 characters"
        )
        String password

) {
}