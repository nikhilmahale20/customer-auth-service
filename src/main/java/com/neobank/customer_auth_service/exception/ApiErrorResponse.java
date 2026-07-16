package com.neobank.customer_auth_service.exception;

import java.time.OffsetDateTime;

public record ApiErrorResponse(

        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path

) {
}