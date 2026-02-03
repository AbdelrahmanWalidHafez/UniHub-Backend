package com.unihub.auth.security.service;

import com.unihub.auth.security.dto.request.LoginRequest;
import com.unihub.auth.security.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.AuthenticationException;

public interface ITokenProvider {

    LoginResponse generateTokens(LoginRequest loginRequest);

    LoginResponse refresh(String oldRefreshToken) throws AuthenticationException;

    void revokeTokens(HttpServletRequest request, String refreshToken);
}
