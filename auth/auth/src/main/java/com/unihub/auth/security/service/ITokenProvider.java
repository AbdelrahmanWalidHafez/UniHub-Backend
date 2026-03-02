package com.unihub.auth.security.service;

import com.unihub.auth.security.dto.request.ChangeForgotPasswordRequest;
import com.unihub.auth.security.dto.request.LoginRequest;
import com.unihub.auth.security.dto.request.VerificationRequest;
import com.unihub.auth.security.dto.response.LoginResponse;
import com.unihub.auth.security.dto.response.VerificationOpaqueToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface ITokenProvider {

    LoginResponse generateTokens(LoginRequest loginRequest, HttpServletResponse response) ;

    LoginResponse refresh(HttpServletRequest request,HttpServletResponse response) throws AuthenticationException;

    void revokeTokens(HttpServletRequest request, HttpServletResponse response);

    void generateForgotPasswordVerificationCode(String email);

    VerificationOpaqueToken verifyForgotPasswordVerificationCode(VerificationRequest verificationRequest);

    void changeForgotPassword(@Valid ChangeForgotPasswordRequest changeForgotPasswordRequest, Authentication authentication,HttpServletRequest request);
}
