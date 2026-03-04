package com.unihub.auth.security.service;

import com.unihub.auth.security.dto.request.*;
import com.unihub.auth.security.dto.response.LoginResponse;
import com.unihub.auth.security.dto.response.VerificationOpaqueToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface ITokenProvider {

    LoginResponse generateTokens(LoginRequest loginRequest, HttpServletResponse response) ;

    LoginResponse generateTokens(LoginRequest loginRequest);

    LoginResponse refresh(HttpServletRequest request,HttpServletResponse response) throws AuthenticationException;

    LoginResponse refresh(String oldRefreshToken) throws AuthenticationException;

    void revokeTokens(HttpServletRequest request, HttpServletResponse response);

    void revokeTokens(HttpServletRequest request,String refreshToken);

    void generateForgotPasswordVerificationCode(String email);

    void generateActivationCode(String email);

    VerificationOpaqueToken verifyForgotPasswordVerificationCode(VerificationRequest verificationRequest);

    VerificationOpaqueToken verifyActivationCode(ActivationVerificationRequest verificationRequest);

    void changeForgotPassword(ChangeForgotPasswordRequest changeForgotPasswordRequest, Authentication authentication,HttpServletRequest request);

    void setPassword(SetPasswordRequest request);
}
