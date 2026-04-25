package com.unihub.auth.security.filter;

import com.unihub.auth.common.redis.concerns.RedisKeys;
import com.unihub.auth.common.redis.service.IRedisService;
import com.unihub.auth.security.config.JwtConfigurationProperties;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VerificationFilter extends OncePerRequestFilter {

    private final IRedisService redisService;

    private final UserDetailsService userDetailsService;

    private final JwtConfigurationProperties jwtConfigurationProperties;

    @Override
    protected  void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String verificationToken=getVerificationToken(request);
        String email=verifyToken(verificationToken);
        setAuthentication(email);
        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return ! path.equals("/api/v1/auth/change-forgot-password");
    }

    private String getVerificationToken(HttpServletRequest request) {

        String headerValue = request.getHeader(jwtConfigurationProperties.authorizationHeader());
        if (headerValue == null) {
            throw new BadCredentialsException("no token found");
        }
        return headerValue.substring(7);
    }


    private String verifyToken(String verificationToken) {

        if (!redisService.exists(RedisKeys.VERIFICATION_TOKEN_PREFIX + verificationToken)) {
            throw new BadCredentialsException("invalid token received");
        }
        Optional<String> email = validateAndGetUsername(verificationToken);
        if (email.isEmpty()) {
            throw new EntityNotFoundException("no email found");
        }
        return email.get();
    }

    private Optional<String> validateAndGetUsername(String verificationToken) {
        return Optional.ofNullable(redisService.getValue(RedisKeys.VERIFICATION_TOKEN_PREFIX + verificationToken));
    }

    private void setAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        SecurityContextHolder
                .getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

    }
}
