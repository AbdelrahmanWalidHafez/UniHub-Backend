package com.unihub.auth.security.service.impl;

import com.unihub.auth.common.redis.concerns.RedisKeys;
import com.unihub.auth.common.redis.service.IRedisService;
import com.unihub.auth.security.config.JwtConfigurationProperties;
import com.unihub.auth.security.dto.request.*;
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
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProviderImpl implements ITokenProvider {

    @Value("${verification.codeTTL}")
    private  long verificationCodeTTL;

    @Value("${verification.tokenTTL}")
    private  long verificationTokenTTL;

    private final StreamBridge streamBridge;

    private final IRedisService redisService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtConfigurationProperties jwtProperties;

    private final ProjectUserDetailsService userDetailsService;

    private final JwtGenerationContext jwtGenerationContext;

    private final AuthenticationManager authenticationManager;

    private static final SecureRandom secureRandom=new SecureRandom();


    @Override
    public LoginResponse generateTokens(LoginRequest loginRequest, HttpServletResponse response){
        Authentication authentication = authenticate(loginRequest);
        setCookie(generateRefreshToken(authentication.getName()),response);
        return LoginResponse
                .builder()
                .accessToken(generateAccessToken(getSecretKey(jwtProperties.secret()), authentication))
                .build();
    }

    @Override
    public LoginResponse generateTokens(LoginRequest loginRequest){
        Authentication authentication = authenticate(loginRequest);
        return LoginResponse
                .builder()
                .accessToken(generateAccessToken(getSecretKey(jwtProperties.secret()), authentication))
                .refreshToken(generateRefreshToken(authentication.getName()))
                .build();
    }

    @Override
    public LoginResponse refresh(HttpServletRequest request,HttpServletResponse response) throws AuthenticationException {
        String oldRefreshToken=getRefreshTokenFromCookie(request);
        UserDetails userDetails = validateRefreshToken(oldRefreshToken);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        setCookie(rotateRefreshToken(oldRefreshToken,authentication.getName()),response);
        return LoginResponse
                .builder()
                .accessToken(generateAccessToken(getSecretKey(jwtProperties.secret()), authentication))
                .build();
    }

    @Override
    public LoginResponse refresh(String oldRefreshToken) throws AuthenticationException {
        UserDetails userDetails = validateRefreshToken(oldRefreshToken);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return LoginResponse
                .builder()
                .accessToken(generateAccessToken(getSecretKey(jwtProperties.secret()), authentication))
                .refreshToken(rotateRefreshToken(oldRefreshToken,authentication.getName()))
                .build();
    }

    @Override
    public void revokeTokens(HttpServletRequest request, HttpServletResponse response) {
        fetchAndBlackListJwt(request);
        deleteCookie(response);
        revokeRefreshToken(getRefreshTokenFromCookie(request));
    }

    @Override
    public void revokeTokens(HttpServletRequest request,String refreshToken) {
        fetchAndBlackListJwt(request);
        revokeRefreshToken(refreshToken);
    }

    @Override
    public void generateForgotPasswordVerificationCode(String email) {
        redisService.deleteKey(RedisKeys.VERIFICATION_PREFIX +email);
        if (userDetailsService.isExist(email)) {
         generateAndSendVerificationCode(email);
        }
    }

    @Override
    public void generateActivationCode(String email) {
        redisService.deleteKey(RedisKeys.ACTIVATION_PREFIX+email);
        if (userDetailsService.isExist(email)) {
            generateAndSendActivationCode(email);
        }
    }

    @Override
    public VerificationOpaqueToken verifyForgotPasswordVerificationCode(VerificationRequest verificationRequest){
        Optional<String>code=validateAndGetCode(RedisKeys.VERIFICATION_PREFIX+verificationRequest.getEmail());
        if (code.isEmpty()) {
            throw new BadCredentialsException("invalid verification code");
        }
        if (!code.get().equals(verificationRequest.getVerificationCode())) {
            throw new IllegalArgumentException("invalid verification code");
        }
        redisService.deleteKey(RedisKeys.VERIFICATION_PREFIX+verificationRequest.getEmail());
        return generateVerificationOpaqueToken(verificationRequest.getEmail());
    }

    @Override
    public VerificationOpaqueToken verifyActivationCode(ActivationVerificationRequest verificationRequest){
        Optional<String>code=validateAndGetCode(RedisKeys.ACTIVATION_PREFIX+verificationRequest.getEmail());
        if (code.isEmpty()) {
            throw new BadCredentialsException("invalid activation code");
        }
        if (!code.get().equals(verificationRequest.getActivationCode())) {
            throw new IllegalArgumentException("invalid verification code");
        }
        redisService.deleteKey(RedisKeys.ACTIVATION_PREFIX+verificationRequest.getEmail());
        return generateVerificationOpaqueToken(verificationRequest.getEmail());
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public void setPassword(SetPasswordRequest request) {
       Optional<String>email=validateAndGetEmail(RedisKeys.VERIFICATION_TOKEN_PREFIX+request.getVerificationToken());
       if(email.isEmpty()){
           throw  new BadCredentialsException("invalid verification code");
       }
        validatePasswordConfirmation(request.getPassword(), request.getConfirmPassword());
        User user = userRepository.findByEmail(email.get())
                .orElseThrow(() -> new EntityNotFoundException("Resource doesn't exist"));
        user.setAccountNonLocked(true);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        deleteVerificationToken(request.getVerificationToken());
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest changePasswordRequest, Authentication authentication) {
        validatePasswordConfirmation(changePasswordRequest.getPassword(), changePasswordRequest.getConfirmPassword());
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Resource doesn't exist"));
        if (passwordEncoder.matches(changePasswordRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("you can't use your old password");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
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
        redisService.setValue(RedisKeys.REFRESH_PREFIX+refreshToken, username, ttl);
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
        redisService.deleteKey(RedisKeys.REFRESH_PREFIX+refreshToken);
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
        redisService.setValue(RedisKeys.BLACKLIST_PREFIX+ jti, "1", ttl);
    }

    private UniversityMetadata getUniMetaData(Authentication authentication) {
         User user=userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
         return user.getUniversityMetadata();
    }

    private void generateAndSendVerificationCode(String email) {
       String otp=generateOtp();
        redisService.setValue(RedisKeys.VERIFICATION_PREFIX+email,otp, Duration.ofSeconds(verificationCodeTTL));
        streamBridge.send("sendVerificationCode-out-0", SendVerificationCode
                .builder()
                .verificationCode(otp)
                .to(email)
                .expirationTime(verificationCodeTTL)
                .build());
    }

    private void generateAndSendActivationCode(String email) {
        String otp=generateOtp();
        redisService.setValue(RedisKeys.ACTIVATION_PREFIX+email,otp, Duration.ofSeconds(verificationCodeTTL));
        streamBridge.send("sendActivationCode-out-0", SendActivationCode
                .builder()
                .activationCode(otp)
                .to(email)
                .expirationTime(verificationCodeTTL)
                .build());
    }

    private VerificationOpaqueToken generateVerificationOpaqueToken(String email) {
        String verificationOpaqueToken = UUID.randomUUID() + "-" + UUID.randomUUID();
        redisService.setValue(RedisKeys.VERIFICATION_TOKEN_PREFIX+ verificationOpaqueToken, email, Duration.ofSeconds(verificationTokenTTL));
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

    private Optional<String> validateAndGetCode(String key){
        return Optional.ofNullable(redisService.getValue(key));
    }

    private void deleteVerificationToken(HttpServletRequest request){
        String verification_token=request.getHeader(jwtProperties.authorizationHeader()).substring(7);
        deleteVerificationToken(verification_token);
    }

    private void deleteVerificationToken(String verificationToken){
        redisService.deleteKey(RedisKeys.VERIFICATION_TOKEN_PREFIX+verificationToken);
    }

    private void setCookie(RefreshToken token,HttpServletResponse response){
        Cookie cookie=new Cookie("refreshToken",token.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/api/v1/auth");
        cookie.setMaxAge((int)(token.getExpiresIn() / 1000));
        cookie.setAttribute("SameSite", "Lax");
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
        cookie.setPath("/api/v1/auth");
        cookie.setMaxAge((0));
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);
    }

    private Authentication authenticate(LoginRequest request){
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    }

    private UserDetails validateRefreshToken(String oldRefreshToken){
        Optional<String> username = validateAndGetEmail(RedisKeys.REFRESH_PREFIX+oldRefreshToken);
        if (username.isEmpty()) {
            throw new AuthenticationException("invalid refresh token received") {
                @Override
                public @Nullable Authentication getAuthenticationRequest() {
                    return super.getAuthenticationRequest();
                }
            } ;

        }
        return userDetailsService.loadUserByUsername(username.get());
    }

    private void fetchAndBlackListJwt(HttpServletRequest request){
        String jwt = request.getHeader(jwtProperties.authorizationHeader()).substring(7);
        Duration ttl = Duration.ofMillis(getExpirationDate(jwt, getSecretKey(jwtProperties.secret())).getTime() - System.currentTimeMillis());
        if (!ttl.isNegative() && !ttl.isZero()) {
            blackListToken(jwt, ttl);
        }
    }

    private String generateOtp(){
        StringBuilder stringBuilder=new StringBuilder();
        for (int i = 0; i <6 ; i++) {
            stringBuilder.append(secureRandom.nextInt(10));
        }
        return stringBuilder.toString();
    }
}

