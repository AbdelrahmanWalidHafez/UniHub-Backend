package com.unihub.auth.security.strategy.context;

import com.unihub.auth.security.config.JwtConfigurationProperties;
import com.unihub.auth.security.model.UniversityMetadata;
import com.unihub.auth.security.strategy.JwtGenerationStrategy;
import com.unihub.auth.security.strategy.impl.GeneralStrategy;
import com.unihub.auth.security.strategy.impl.SystemAdminStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class JwtGenerationContext {

    private final GeneralStrategy generalStrategy;

    private final SystemAdminStrategy systemAdminGenerationStrategy;

    private final JwtConfigurationProperties jwtProperties;

    public String performJwtGeneration(Authentication authentication, UniversityMetadata universityMetadata, SecretKey key) {
        return getStrategy(authentication).generateJwt(authentication, universityMetadata, key,jwtProperties.expirationTime());
    }

    private JwtGenerationStrategy getStrategy(Authentication authentication) {
        if(authentication
                .getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_SYSTEM_ADMIN"))){
            return this.systemAdminGenerationStrategy;
        }else{
            return this.generalStrategy;
        }

    }
}
