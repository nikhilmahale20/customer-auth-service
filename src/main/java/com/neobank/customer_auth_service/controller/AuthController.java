package com.neobank.customer_auth_service.controller;

import com.neobank.customer_auth_service.dto.request.ForgotPasswordRequest;
import com.neobank.customer_auth_service.dto.request.LoginRequest;
import com.neobank.customer_auth_service.dto.request.VerifyOtpRequest;
import com.neobank.customer_auth_service.dto.request.VerifyResetOtpRequest;
import com.neobank.customer_auth_service.dto.request.ResetPasswordRequest;

import com.neobank.customer_auth_service.dto.response.ApiResponse;
import com.neobank.customer_auth_service.dto.response.LoginResponse;

import com.neobank.customer_auth_service.dto.response.TokenValidationResponse;
import com.neobank.customer_auth_service.service.AuthService;
import com.neobank.customer_auth_service.service.JwtService;
import com.neobank.customer_auth_service.service.OtpService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final OtpService otpService;

    private final JwtService jwtService;



    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {

        LoginResponse response =
                authService.login(request);

        ApiResponse<LoginResponse> apiResponse =
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Login successful",
                        response
                );

        return ResponseEntity.ok(
                apiResponse
        );
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Void>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request
    ) {

        otpService.verifyOtp(
                request.customerId(),
                request.otpCode()
        );

        ApiResponse<Void> apiResponse =
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "OTP verified successfully. Customer account activated.",
                        null
                );

        return ResponseEntity.ok(
                apiResponse
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {

        String customerId =
                otpService.generatePasswordResetOtp(
                        request.getEmail()
                );

        ApiResponse<String> apiResponse =
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Password reset OTP generated successfully",
                        customerId
                );

        return ResponseEntity.ok(
                apiResponse
        );
    }

    @PostMapping("/verify-reset-otp")
    public ResponseEntity<ApiResponse<Void>> verifyResetOtp(
            @Valid @RequestBody VerifyResetOtpRequest request
    ) {

        otpService.verifyPasswordResetOtp(
                request.getCustomerId(),
                request.getOtpCode()
        );

        ApiResponse<Void> apiResponse =
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Password reset OTP verified successfully",
                        null
                );

        return ResponseEntity.ok(
                apiResponse
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid
            @RequestBody ResetPasswordRequest request
    ) {

        authService.resetPassword(
                request
        );

        ApiResponse<Void> apiResponse =
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Password reset successfully",
                        null
                );

        return ResponseEntity.ok(
                apiResponse
        );
    }

    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<TokenValidationResponse>>
    validateToken(
            @RequestHeader("Authorization")
            String authorizationHeader
    ) {

        String token =
                authorizationHeader.substring(
                        7
                );

        boolean valid =
                jwtService.isTokenValid(
                        token
                );

        TokenValidationResponse response;

        if (valid) {

            response =
                    new TokenValidationResponse(
                            true,
                            jwtService.extractUsername(
                                    token
                            ),
                            jwtService.extractCustomerId(
                                    token
                            ),
                            jwtService.extractRole(
                                    token
                            )
                    );

        } else {

            response =
                    new TokenValidationResponse(
                            false,
                            null,
                            null,
                            null
                    );
        }

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Token validation completed",
                        response
                )
        );
    }
}