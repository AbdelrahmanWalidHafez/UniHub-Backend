package com.unihub.auth.security.strategy.context;

import com.unihub.auth.security.config.JwtConfigurationProperties;
import com.unihub.auth.security.model.UniversityMetadata;
import com.unihub.auth.security.strategy.JwtGenerationStrategy;
import com.unihub.auth.security.strategy.impl.CustomerServiceStrategy;
import com.unihub.auth.security.strategy.impl.SystemAdminStrategy;
import com.unihub.auth.security.strategy.impl.UserStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtGenerationContext {

    private final JwtConfigurationProperties jwtProperties;

    private final UserStrategy userStrategy;

    private final CustomerServiceStrategy customerServiceStrategy;

    private final SystemAdminStrategy systemAdminGenerationStrategy;

    public String performJwtGeneration(Authentication authentication, UniversityMetadata universityMetadata, SecretKey key)  {
        return getStrategy(authentication).generateJwt(authentication, universityMetadata, key,jwtProperties.expirationTime());
    }

    private JwtGenerationStrategy getStrategy(Authentication authentication) {
        if(authentication
                .getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> Objects.equals(grantedAuthority.getAuthority(), "ROLE_SYSTEM_ADMIN"))){
            return this.systemAdminGenerationStrategy;
        }else if(authentication
                .getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> Objects.equals(grantedAuthority.getAuthority(), "ROLE_CUSTOMER_SERVICE"))){
            return this.customerServiceStrategy;
        }else{
            return this.userStrategy;
        }

    }
}
