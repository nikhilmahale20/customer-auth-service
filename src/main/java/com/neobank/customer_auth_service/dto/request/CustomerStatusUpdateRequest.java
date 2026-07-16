package com.neobank.customer_auth_service.dto.request;

import com.neobank.customer_auth_service.model.CustomerStatus;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerStatusUpdateRequest {

    @NotNull(
            message = "Customer status is required"
    )
    private CustomerStatus status;
}