package com.neobank.customer_auth_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerUpdateRequest(

        @NotBlank(message = "Name is required")
        @Size(
                min = 2,
                max = 100,
                message = "Name must be between 2 and 100 characters"
        )
        String name,

        @NotBlank(message = "Phone is required")
        @Pattern(
                regexp = "^[0-9]{10}$",
                message = "Phone must contain exactly 10 digits"
        )
        String phone,

        @NotBlank(message = "Address is required")
        @Size(
                min = 5,
                max = 255,
                message = "Address must be between 5 and 255 characters"
        )
        String address

) {
}