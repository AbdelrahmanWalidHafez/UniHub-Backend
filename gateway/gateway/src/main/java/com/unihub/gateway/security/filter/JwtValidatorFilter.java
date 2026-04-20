package com.unihub.gateway.security.filter;


import com.unihub.gateway.common.redis.concerns.RedisKeys;
import com.unihub.gateway.common.redis.serivce.IRedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class JwtValidatorFilter implements WebFilter {

    private final IRedisService redisService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.authorizationHeader}")
    private String authHeader;

    private static final List<String> PUBLIC_PATHS = List.of(
            "/actuator",
            "/unihub/subscription/api/v1/public/request-subscription",
            "/unihub/subscription/api/v1/subscription-plans/all",
            "/unihub/subscription/api/v1/inquiries/public"
    );
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = exchange.getRequest().getURI().getPath();
        if (PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }
        try{
            String jwt = extractJwt(request);
            Claims claims=getClaims(jwt);
            checkBlackListedToken(claims);
            return chain
                    .filter(exchange.mutate()
                            .request(request.mutate()
                                    .header("X-User-Email", claims.get("email", String.class))
                                    .header("X-User-University-Id", claims.get("university_id", String.class))
                                    .header("X-User-College-Id", claims.get("college_id", String.class))
                                    .build()
                            )
                            .build())
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(generateUsernamePasswordAuthenticationToken(claims)));

        }catch(Exception e){
            return unauthorized(exchange,e);
        }
    }

    private String extractJwt(ServerHttpRequest request) {
        String authHeaderValue = request.getHeaders().getFirst(authHeader);
        if(authHeaderValue == null||!authHeaderValue.startsWith("Bearer ")) {
            throw new BadCredentialsException("invalid token received");
        }
        return authHeaderValue.substring(7);
    }
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private Claims getClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private void checkBlackListedToken(Claims claims) {
        String jti = claims.getId();
        if (redisService.exists(RedisKeys.BLACKLIST_PREFIX+ jti)) {
            throw new BadCredentialsException("invalid token received");
        }
    }

    private Authentication generateUsernamePasswordAuthenticationToken(Claims claims) {
        try {
            String email = String.valueOf(claims.get("email"));
            List<String> authorities =getAuthorities(claims);
            return new UsernamePasswordAuthenticationToken(email, null,AuthorityUtils.createAuthorityList(authorities));
        } catch (Exception e) {
            throw new BadCredentialsException("invalid token received");
        }
    }
    private List<String> getAuthorities(Claims claims) {
        String authorities = String.valueOf(claims.get("authorities"));
        List<String> authoritiesList = new ArrayList<>(claims.entrySet().stream()
                .filter(entry -> entry.getValue() instanceof Boolean)
                .filter(entry -> Boolean.TRUE.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .toList());
        authoritiesList.add(authorities);
        return authoritiesList;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, Exception ex) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        String body = "{\"error\": \"" + ex.getMessage() + "\"}";
        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(body.getBytes(StandardCharsets.UTF_8))));
    }

}


