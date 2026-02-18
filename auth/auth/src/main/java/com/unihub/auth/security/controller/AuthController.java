package com.unihub.auth.security.controller;

import com.unihub.auth.common.exception.dto.ErrorResponseDto;
import com.unihub.auth.security.dto.request.*;
import com.unihub.auth.security.dto.response.LoginResponse;
import com.unihub.auth.security.dto.response.UserDto;
import com.unihub.auth.security.dto.response.VerificationResponse;
import com.unihub.auth.security.service.ITokenProvider;
import com.unihub.auth.security.service.impl.ProjectUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "auth API",
        description = "APIs for managing a user's login,logout,refresh and fetching a user specific data"
)
public class AuthController {

    private final ITokenProvider tokenProvider;

    private final ProjectUserDetailsService userDetailsService;

    @Operation(
            summary = "login",
            description = "Enables a  user to login into a system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "user is logged in successfully",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "unauthorized",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "bad request",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(tokenProvider.generateTokens(loginRequest));
    }

    @Operation(
            summary = "refresh",
            description = "Enables a  user to refresh his/her tokens"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "user is refreshed ",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "unauthorized/invalid token",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "bad request",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshTokenDto refreshRequest) {
        return ResponseEntity.ok(tokenProvider.refresh(refreshRequest.getRefreshToken()));
    }

    @Operation(
            summary = "logout",
            description = "revokes user jwt tokens"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "user tokens revoked successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "unauthorized",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "bad request",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody  RefreshTokenDto logoutRequest, HttpServletRequest request) {
        tokenProvider.revokeTokens(request, logoutRequest.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "fetch user data",
            description = "Enables a  user to fetch his data system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "user data fetched successfully",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "unauthorized",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @GetMapping("/user-info")
    public ResponseEntity<UserDto>  getUserInfo(Authentication authentication) {
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
