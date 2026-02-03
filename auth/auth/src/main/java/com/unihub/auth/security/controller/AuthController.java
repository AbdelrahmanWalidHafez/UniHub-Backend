package com.unihub.auth.security.controller;

import com.unihub.auth.security.dto.request.LoginRequest;
import com.unihub.auth.security.dto.request.RefreshTokenDto;
import com.unihub.auth.security.dto.response.LoginResponse;
import com.unihub.auth.security.dto.response.UserDto;
import com.unihub.auth.security.service.ITokenProvider;
import com.unihub.auth.security.service.impl.ProjectUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final ITokenProvider tokenProvider;

    private final ProjectUserDetailsService userDetailsService;
    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(tokenProvider.generateTokens(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshTokenDto refreshRequest) {
        return ResponseEntity.ok(tokenProvider.refresh(refreshRequest.getRefreshToken()));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody  RefreshTokenDto logoutRequest, HttpServletRequest request) {
        tokenProvider.revokeTokens(request, logoutRequest.getRefreshToken());
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/user-info")
    public ResponseEntity<UserDto>  getUserInfo(Authentication authentication) {
        return ResponseEntity.ok(userDetailsService.getUserInfo(authentication));
    }

}
