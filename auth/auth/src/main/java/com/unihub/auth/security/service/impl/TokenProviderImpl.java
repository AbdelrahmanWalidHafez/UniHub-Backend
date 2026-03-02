package com.unihub.auth.security.service.impl;

import com.unihub.auth.common.redis.service.IRedisService;
import com.unihub.auth.security.dto.request.LoginRequest;
import com.unihub.auth.security.dto.response.AccessToken;
import com.unihub.auth.security.dto.response.LoginResponse;
import com.unihub.auth.security.dto.response.RefreshToken;
import com.unihub.auth.security.model.UniversityMetadata;
import com.unihub.auth.security.model.User;
import com.unihub.auth.security.repository.UserRepository;
import com.unihub.auth.security.service.ITokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenProviderImpl implements ITokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.authorizationHeader}")
    private String authHeader;

    @Value("${jwt.expirationTime}")
    private String expirationTime;

    @Value("${jwt.refreshToken.expirationTime}")
    private String refreshTokenExpirationTime;

    private final IRedisService redisService;

    private final UserRepository userRepository;

    private final UserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    /**
     *
     * @deprecated : not secured
     *
     */
    @Deprecated(
            forRemoval = true,
            since = "1.2.1"
    )
    @Override
    public LoginResponse generateTokens(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        return LoginResponse
                .builder()
                .accessToken(generateAccessToken(getSecretKey(jwtSecret), authentication))
                .refreshToken(generateRefreshToken(authentication.getName()))
                .build();
    }

    /**
     *
     * @deprecated : not secured
     *
     */
    @Deprecated(
            forRemoval = true,
            since = "1.2.1"
    )
    @Override
    public LoginResponse refresh(String oldRefreshToken) throws AuthenticationException {
        Optional<String> username = validateAndGetEmail("refresh:" + oldRefreshToken);
        if (username.isEmpty()) {
            throw new AuthenticationException("invalid refresh token received") {
                @Override
                public @Nullable Authentication getAuthenticationRequest() {
                    return super.getAuthenticationRequest();
                }
            } ;

        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username.get());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return LoginResponse
                .builder()
                .accessToken(generateAccessToken(getSecretKey(jwtSecret), authentication))
                .refreshToken(rotateRefreshToken(oldRefreshToken, authentication.getName()))
                .build();
    }

    /**
     *
     * @deprecated : not secured
     *
     */
    @Deprecated(
            forRemoval = true,
            since = "1.2.1"
    )
    @Override
    public void revokeTokens(HttpServletRequest request, String refreshToken) {
        String jwt = request.getHeader(authHeader).substring(7);
        Duration ttl = Duration.ofMillis(getExpirationDate(jwt, getSecretKey(jwtSecret)).getTime() - System.currentTimeMillis());
        if (!ttl.isNegative() && !ttl.isZero()) {
            blackListToken(jwt, ttl);
        }
        revokeRefreshToken(refreshToken);
    }

    private AccessToken generateAccessToken(SecretKey key, Authentication authentication) {
       UniversityMetadata userUniversityMetadata = getUniMetaData(authentication);
        String jwt = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer("uniHub")
                .setSubject("access-token")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + Integer.parseInt(expirationTime)))
                .claim("email", authentication.getName())
                .claim("authorities", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .claim("university_id",
                        (userUniversityMetadata != null ?
                         userUniversityMetadata.getTid() : "N/A"))
                .signWith(key).compact();
        return AccessToken
                .builder()
                .tokenType("Bearer")
                .accessToken(jwt)
                .expiresIn(Integer.parseInt(expirationTime)).build();
    }

    private RefreshToken generateRefreshToken(String username) {
        String refreshToken = UUID.randomUUID() + "-" + UUID.randomUUID();
        Duration ttl = Duration.ofMillis(Integer.parseInt(refreshTokenExpirationTime));
        redisService.setValue("refresh:"+ refreshToken, username, ttl);
        return RefreshToken
                .builder()
                .tokenType("Opaque")
                .refreshToken(refreshToken)
                .expiresIn(Integer.parseInt(refreshTokenExpirationTime))
                .build();
    }

    private SecretKey getSecretKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private void revokeRefreshToken(String refreshToken) {
        redisService.deleteKey("refresh:" + refreshToken);
    }

    private Optional<String> validateAndGetEmail(String key) {
        return Optional.ofNullable(redisService.getValue(key));
    }

    private RefreshToken rotateRefreshToken(String oldToken, String username) {
        revokeRefreshToken(oldToken);
        return generateRefreshToken(username);
    }

    private Date getExpirationDate(String token, SecretKey key) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getExpiration();
    }

    private void blackListToken(String jwt, Duration ttl) {
        String jti=Jwts.parserBuilder().setSigningKey(getSecretKey(jwtSecret)).build().parseClaimsJws(jwt).getBody().getId();
        redisService.setValue("blacklist:access:"+ jti, "1", ttl);
    }

    private UniversityMetadata getUniMetaData(Authentication authentication) {
         User user=userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
         if(user.getUniversityMetadata()!=null) {
             return user.getUniversityMetadata();
         }
         return null;
    }
}

