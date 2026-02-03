package com.unihub.auth.security.filter;


import com.unihub.auth.common.redis.service.IRedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtValidatorFilter extends OncePerRequestFilter {

    private final IRedisService redisService;

    @Value("${jwt.secret}")
    String secret;

    @Value("${jwt.authorizationHeader}")
    private String authHeader;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt =extractJwt(request);
            Claims claims = getClaims(jwt);
            checkBlackListedToken(claims);
            parse(claims);
            filterChain.doFilter(request, response);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException(ex.getMessage());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return  path.equals("/api/v1/auth/login") ||
                path.startsWith("/actuator") ||
                path.equals("/api/v1/auth/refresh")||
                path.startsWith("/api/v1/internal");
    }

    private String extractJwt(HttpServletRequest request) {
        String authHeaderValue = request.getHeader(authHeader);
        if(authHeaderValue == null||!authHeaderValue.startsWith("Bearer ")) {
            throw new BadCredentialsException("invalid token received");
        }
        return authHeaderValue.substring(7);
    }

    private void parse(Claims claims) {
        try {

            String email = String.valueOf(claims.get("email"));
            String authorities = String.valueOf(claims.get("authorities"));
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(email, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)));
        } catch (Exception e) {
            throw new BadCredentialsException("invalid token received");
        }
    }

    private Claims getClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private void checkBlackListedToken(Claims claims) {
        String jti = claims.getId();
        if (redisService.exists("blacklist:access:" + jti)) {
            throw new BadCredentialsException("invalid token received");
        }
    }

}

