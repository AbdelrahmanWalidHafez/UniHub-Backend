package com.unihub.auth.security.strategy.impl;

import com.unihub.auth.security.model.UniversityMetadata;
import com.unihub.auth.security.strategy.JwtGenerationStrategy;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerServiceStrategy implements JwtGenerationStrategy {

    @Override
    public String generateJwt(Authentication authentication, UniversityMetadata universityMetadata, SecretKey key, long expiration) {
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer("uniHub")
                .setSubject("access-token")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration))
                .claim("email", authentication.getName())
                .claim("authorities", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .claim("university_id",
                        (universityMetadata != null ?
                                universityMetadata.getTid() : "N/A"))
                .signWith(key).compact();
    }
}
