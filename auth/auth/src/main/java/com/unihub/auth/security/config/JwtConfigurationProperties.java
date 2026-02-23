package com.unihub.auth.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtConfigurationProperties(
        String secret,
        long expirationTime,
        String authorizationHeader,
        RefreshToken refreshToken
) {

    public record RefreshToken(
            long expirationTime
    ) {}
}
