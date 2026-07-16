package com.neobank.customer_auth_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {

    private OffsetDateTime timestamp;

    private int status;

    private String message;

    private T data;

    private boolean success;



    public static <T> ApiResponse<T> success(
            int status,
            String message,
            T data
    ) {

        return new ApiResponse<>(
                OffsetDateTime.now(),
                status,
                message,
                data,
                true
        );
    }

    public static <T> ApiResponse<T> failure(
            int status,
            String message,
            T data
    ) {

        return new ApiResponse<>(
                OffsetDateTime.now(),
                status,
                message,
                data,
                false
        );
    }
}