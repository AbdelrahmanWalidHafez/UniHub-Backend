package com.unihub.auth.security.service;

import com.unihub.auth.security.dto.request.ChangeForgotPasswordRequest;
import com.unihub.auth.security.dto.request.LoginRequest;
import com.unihub.auth.security.dto.request.VerificationRequest;
import com.unihub.auth.security.dto.response.LoginResponse;
import com.unihub.auth.security.dto.response.VerificationOpaqueToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface ITokenProvider {

    LoginResponse generateTokens(LoginRequest loginRequest);

    LoginResponse refresh(String oldRefreshToken) throws AuthenticationException;

    void revokeTokens(HttpServletRequest request, String refreshToken);

    void generateForgotPasswordVerificationCode(String email);

    VerificationOpaqueToken verifyForgotPasswordVerificationCode(VerificationRequest verificationRequest);

    void changeForgotPassword(@Valid ChangeForgotPasswordRequest changeForgotPasswordRequest, Authentication authentication);
}
