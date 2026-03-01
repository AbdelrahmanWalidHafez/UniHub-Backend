package com.unihub.auth.security.strategy;

import com.unihub.auth.security.model.UniversityMetadata;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;

public interface JwtGenerationStrategy {

    String generateJwt(Authentication authentication, UniversityMetadata universityMetadata, SecretKey key,long expiration);
}
