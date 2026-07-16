package com.neobank.customer_auth_service.service;

import com.neobank.customer_auth_service.dto.request.ChangePasswordRequest;
import com.neobank.customer_auth_service.dto.request.LoginRequest;
import com.neobank.customer_auth_service.dto.request.ResetPasswordRequest;
import com.neobank.customer_auth_service.dto.response.LoginResponse;

public interface AuthService {

	LoginResponse login(
			LoginRequest request
	);

	void changePassword(
			String customerId,
			ChangePasswordRequest request
	);

	void resetPassword(
			ResetPasswordRequest request
	);
}