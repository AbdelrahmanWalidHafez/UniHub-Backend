package com.unihub.auth.security.service.impl;

import com.unihub.auth.common.redis.service.IRedisService;
import com.unihub.auth.security.config.JwtConfigurationProperties;
import com.unihub.auth.security.dto.request.ChangeForgotPasswordRequest;
import com.unihub.auth.security.dto.request.LoginRequest;
import com.unihub.auth.security.dto.request.SendVerificationCode;
import com.unihub.auth.security.dto.request.VerificationRequest;
import com.unihub.auth.security.dto.response.AccessToken;
import com.unihub.auth.security.dto.response.LoginResponse;
import com.unihub.auth.security.dto.response.RefreshToken;
import com.unihub.auth.security.dto.response.VerificationOpaqueToken;
import com.unihub.auth.security.model.UniversityMetadata;
import com.unihub.auth.security.model.User;
import com.unihub.auth.security.repository.UserRepository;
import com.unihub.auth.security.service.ITokenProvider;
import com.unihub.auth.security.strategy.context.JwtGenerationContext;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenProviderImpl implements ITokenProvider {

    private final StreamBridge streamBridge;

    private final IRedisService redisService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${verification.codeTTL}")
    private  long verificationCodeTTL;

    @Value("${verification.tokenTTL}")
    private  long verificationTokenTTL;

    private final JwtConfigurationProperties jwtProperties;

    private final ProjectUserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    private final JwtGenerationContext jwtGenerationContext;


    @Override
    public LoginResponse generateTokens(LoginRequest loginRequest, HttpServletResponse response){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        setCookie(generateRefreshToken(authentication.getName()),response);
        return LoginResponse
                .builder()
                .accessToken(generateAccessToken(getSecretKey(jwtProperties.secret()), authentication))
                .build();
    }

    @Override
    public LoginResponse generateTokens(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        return LoginResponse
                .builder()
                .accessToken(generateAccessToken(getSecretKey(jwtProperties.secret()), authentication))
                .refreshToken(generateRefreshToken(authentication.getName()))
                .build();
    }

    @Override
    public LoginResponse refresh(HttpServletRequest request,HttpServletResponse response) throws AuthenticationException {
        String oldRefreshToken=getRefreshTokenFromCookie(request);
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
        setCookie(rotateRefreshToken(oldRefreshToken,authentication.getName()),response);
        return LoginResponse
                .builder()
                .accessToken(generateAccessToken(getSecretKey(jwtProperties.secret()), authentication))
                .build();
    }

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
                .accessToken(generateAccessToken(getSecretKey(jwtProperties.secret()), authentication))
                .refreshToken(rotateRefreshToken(oldRefreshToken,authentication.getName()))
                .build();
    }

    @Override
    public void revokeTokens(HttpServletRequest request, HttpServletResponse response) {
        String jwt = request.getHeader(jwtProperties.authorizationHeader()).substring(7);
        Duration ttl = Duration.ofMillis(getExpirationDate(jwt, getSecretKey(jwtProperties.secret())).getTime() - System.currentTimeMillis());
        if (!ttl.isNegative() && !ttl.isZero()) {
            blackListToken(jwt, ttl);
        }
        deleteCookie(response);
        revokeRefreshToken(getRefreshTokenFromCookie(request));
    }

    @Override
    public void revokeTokens(HttpServletRequest request,String refreshToken) {
        String jwt = request.getHeader(jwtProperties.authorizationHeader()).substring(7);
        Duration ttl = Duration.ofMillis(getExpirationDate(jwt, getSecretKey(jwtProperties.secret())).getTime() - System.currentTimeMillis());
        if (!ttl.isNegative() && !ttl.isZero()) {
            blackListToken(jwt, ttl);
        }
        revokeRefreshToken(refreshToken);
    }

    @Override
    public void generateForgotPasswordVerificationCode(String email) {
        if(redisService.exists("verification:"+email)){
            redisService.deleteKey("verification"+email);
        }
        if (userDetailsService.isExist(email)) {
         generateAndSendVerificationCode(email);
        }
    }

    @Override
    public VerificationOpaqueToken verifyForgotPasswordVerificationCode(VerificationRequest verificationRequest){
        Optional<String>code=validateAndGetVerificationCode("verification:"+verificationRequest.getEmail());
        if (code.isEmpty()) {
            throw new BadCredentialsException("invalid verification code");
        }
        if (!code.get().equals(verificationRequest.getVerificationCode())) {
            throw new IllegalArgumentException("invalid verification code");
        }
        redisService.deleteKey("verification:"+verificationRequest.getEmail());
        return generateVerificationOpaqueToken(verificationRequest.getEmail());
    }

    @Override
    public void changeForgotPassword(ChangeForgotPasswordRequest changeForgotPasswordRequest, Authentication authentication,HttpServletRequest request) {
        validatePasswordConfirmation(changeForgotPasswordRequest.getPassword(), changeForgotPasswordRequest.getConfirmPassword());
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Resource doesn't exist"));
        if (passwordEncoder.matches(changeForgotPasswordRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("you can't use your old password");
        }
        user.setPassword(passwordEncoder.encode(changeForgotPasswordRequest.getPassword()));
        userRepository.save(user);
        deleteVerificationToken(request);
    }

    private AccessToken generateAccessToken(SecretKey key, Authentication authentication)  {
       UniversityMetadata userUniversityMetadata = getUniMetaData(authentication);
        String jwt = jwtGenerationContext.performJwtGeneration(authentication,userUniversityMetadata,key);
        return AccessToken
                .builder()
                .tokenType("Bearer")
                .accessToken(jwt)
                .expiresIn(jwtProperties.expirationTime()).build();
    }

    private RefreshToken generateRefreshToken(String username) {
        String refreshToken = UUID.randomUUID() + "-" + UUID.randomUUID();
        Duration ttl = Duration.ofMillis(jwtProperties.refreshToken().expirationTime());
        redisService.setValue("refresh:"+ refreshToken, username, ttl);
        return RefreshToken
                .builder()
                .tokenType("Opaque")
                .refreshToken(refreshToken)
                .expiresIn(jwtProperties.refreshToken().expirationTime())
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
        String jti=Jwts.parserBuilder().setSigningKey(getSecretKey(jwtProperties.secret())).build().parseClaimsJws(jwt).getBody().getId();
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

    private void generateAndSendVerificationCode(String email) {
        StringBuilder stringBuilder=new StringBuilder();
        SecureRandom random=new SecureRandom();

        for (int i = 0; i <6 ; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        redisService.setValue("verification:"+email,stringBuilder.toString(), Duration.ofSeconds(verificationCodeTTL));
        streamBridge.send("sendVerificationCode-out-0", SendVerificationCode
                .builder()
                .verificationCode(stringBuilder.toString())
                .to(email)
                .expirationTime(verificationCodeTTL)
                .build());
    }

    private VerificationOpaqueToken generateVerificationOpaqueToken(String email) {
        String verificationOpaqueToken = UUID.randomUUID() + "-" + UUID.randomUUID();
        redisService.setValue("verification_token:"+ verificationOpaqueToken, email, Duration.ofSeconds(verificationTokenTTL));
        return VerificationOpaqueToken
                .builder()
                .token(verificationOpaqueToken)
                .tokenType("opaque")
                .expiresIn(verificationTokenTTL)
                .build();
    }

    private  void validatePasswordConfirmation(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("passwords do not match");
        }
    }

    private Optional<String> validateAndGetVerificationCode(String key){
        return Optional.ofNullable(redisService.getValue(key));
    }

    private void deleteVerificationToken(HttpServletRequest request){
        String verification_token=request.getHeader(jwtProperties.authorizationHeader()).substring(7);
        redisService.deleteKey("verification_token:"+verification_token);
    }

    private void setCookie(RefreshToken token,HttpServletResponse response){
        Cookie cookie=new Cookie("refreshToken",token.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/api/v1/auth/refresh");
        cookie.setMaxAge((int)token.getExpiresIn());
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);
    }

    private String getRefreshTokenFromCookie(HttpServletRequest request){
        String oldRefreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    oldRefreshToken = cookie.getValue();
                    break;
                }
            }
        }
        return oldRefreshToken;
    }

    private void deleteCookie(HttpServletResponse response){
        Cookie cookie=new Cookie("refreshToken",null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/api/auth/v1/refresh");
        cookie.setMaxAge((0));
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);
    }
}

