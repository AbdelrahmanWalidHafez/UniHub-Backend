package com.unihub.auth.security.strategy.impl;

import com.unihub.auth.common.exception.model.SubscriptionException;
import com.unihub.auth.internal.client.UniversityFeignClient;
import com.unihub.auth.internal.dto.response.UniversityResponse;
import com.unihub.auth.security.model.UniversityMetadata;
import com.unihub.auth.security.strategy.JwtGenerationStrategy;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserStrategy implements JwtGenerationStrategy {

    private final UniversityFeignClient universityFeignClient;


    @Override
    public String generateJwt(Authentication authentication, UniversityMetadata universityMetadata, SecretKey key, long expiration) {
        JwtBuilder jwt= Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer("uniHub")
                .setSubject("access-token")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration))
                .claim("IS_ACTIVE",true)
                .claim("email", authentication.getName())
                .claim("authorities", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .claim("university_id",
                        (universityMetadata != null ?
                                universityMetadata.getTid() : "N/A"))
                .claim("college_id",
                        (universityMetadata != null ?
                                universityMetadata.getCid() : "N/A"))
                .signWith(key);
        try{
            handleRequest(universityMetadata);
        }catch (SubscriptionException e) {
            throw new AccessDeniedException("There is no Current Active Subscription Contact Your Administrator.");
        }
        return jwt.compact();
    }

    private void handleRequest(UniversityMetadata universityMetadata) {
        UniversityResponse university = fetchUniversityResponse(universityMetadata)
                .orElseThrow(() -> new SubscriptionException("University has no subscription"));
        if (university.getSubscriptionPlan() == null) {
            throw new SubscriptionException("No subscription plan assigned");
        }
        if (!university.getSubscriptionPlan().getEndDate().isAfter(LocalDate.now())) {
            throw new SubscriptionException("Subscription Date Exceeded or there is no current Subscription Plan");
        }
    }

    private Optional<UniversityResponse> fetchUniversityResponse(UniversityMetadata universityMetadata) {
        return Optional.ofNullable(universityFeignClient.fetchUniversity(universityMetadata.getTid()).getBody());
    }
}
