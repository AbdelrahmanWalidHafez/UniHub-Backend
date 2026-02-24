package com.unihub.auth.security.controller;

import com.unihub.auth.security.dto.request.*;
import com.unihub.auth.security.dto.response.LoginResponse;
import com.unihub.auth.security.dto.response.UserDto;
import com.unihub.auth.security.dto.response.VerificationResponse;
import com.unihub.auth.security.service.ITokenProvider;
import com.unihub.auth.security.service.impl.ProjectUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "logout",
            description = "revokes user jwt tokens"
    )

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody  RefreshTokenDto logoutRequest, HttpServletRequest request) {
        tokenProvider.revokeTokens(request, logoutRequest.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user-info")
    public ResponseEntity<UserDto>getUserInfo(Authentication authentication) {
        return ResponseEntity.ok(userDetailsService.getUserInfo(authentication));
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request){
        tokenProvider.generateForgotPasswordVerificationCode(request.getEmail());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verify-forgot-password-token")
    public ResponseEntity<VerificationResponse> verifyForgotPassword(@Valid @RequestBody VerificationRequest verificationRequest) {
        return ResponseEntity
                .ok(VerificationResponse
                        .builder()
                        .verificationOpaqueToken(tokenProvider.verifyForgotPasswordVerificationCode(verificationRequest))
                        .build());
    }

    @PatchMapping("/change-forgot-password")
    public ResponseEntity<?> changeForgotPassword(@Valid @RequestBody ChangeForgotPasswordRequest changeForgotPasswordRequest, Authentication authentication, HttpServletRequest request) {
        tokenProvider.changeForgotPassword(changeForgotPasswordRequest, authentication,request);
        return ResponseEntity.noContent().build();
    }
}
