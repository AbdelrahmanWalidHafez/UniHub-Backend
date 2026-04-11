package com.unihub.gateway.security.config;

import com.unihub.gateway.security.filter.JwtValidatorFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtValidatorFilter jwtValidatorFilter;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(exchange -> {
            //subscription microservice
            exchange.pathMatchers("/unihub/subscription/api/v1/public/request-subscription").permitAll();
            exchange.pathMatchers("/unihub/subscription/api/v1/customer-service/**").hasRole("CUSTOMER_SERVICE");
            exchange.pathMatchers("/unihub/subscription/api/v1/system-admin/**").hasRole("SYSTEM_ADMIN");
            exchange.pathMatchers("/unihub/subscription/api/v1/admin/**").hasAnyRole("SYSTEM_ADMIN","CUSTOMER_SERVICE");
            exchange.pathMatchers("/unihub/subscription/api/v1/subscription-plans/customer-service/**").hasRole("CUSTOMER_SERVICE");
            exchange.pathMatchers("/unihub/subscription/api/v1/subscription-plans/system-admin/**").hasRole("SYSTEM_ADMIN");
            exchange.pathMatchers("/unihub/subscription/api/v1/subscription-plans/all").permitAll();
            exchange.pathMatchers("/unihub/subscription/api/v1/inquiries/public/**").permitAll();
            exchange.pathMatchers("/unihub/subscription/api/v1/inquiries/customer-service/**").hasRole("CUSTOMER_SERVICE");
            //university management microservice
            exchange.pathMatchers("/unihub/universitymanagement/api/v1/customer-service/**").hasRole("CUSTOMER_SERVICE");
            exchange.pathMatchers("/unihub/universitymanagement/api/v1/colleges/system-admin")
                    .access((mono, context) -> mono
                            .map(auth -> new AuthorizationDecision(
                                    hasRequiredAuthorities(auth, "ROLE_SYSTEM_ADMIN", "IS_ACTIVE")
                            ))
                    );
            exchange.pathMatchers("/unihub/universitymanagement/api/v1/colleges/public/**")
                    .hasAnyRole(
                    "STUDENT",
                    "SECRETARY",
                    "INSTRUCTOR",
                    "SYSTEM_ADMIN"
            );
            exchange.pathMatchers("/unihub/universitymanagement/api/v1/get-university/**").authenticated();
            exchange.pathMatchers("/unihub/universitymanagement/api/v1/internal/**").denyAll();
            //S3 microservice
            exchange.pathMatchers("/unihub/s3/api/v1/get-file/**").authenticated();
            exchange.pathMatchers("/unihub/s3/api/v1/upload-file").denyAll();
            //usage
            exchange.pathMatchers("/unihub/usage/api/v1/usage").hasRole("SYSTEM_ADMIN");
            //announcement
            exchange.pathMatchers("/unihub/announcement/api/v1/posts/secretary/**").hasRole("SECRETARY");
            exchange.pathMatchers("/unihub/announcement/api/v1/posts/public/**").hasAnyRole("SECRETARY","INSTRUCTOR","STUDENT");
            exchange.pathMatchers("/unihub/announcement/api/v1/likes/**").hasAnyRole("SECRETARY","INSTRUCTOR","STUDENT");
            exchange.pathMatchers("/unihub/ai/api/v1/chat/**").hasAnyRole("INSTRUCTOR","STUDENT");
            //classroom
            exchange.pathMatchers("/unihub/classroom/api/v1/classroom/instructor/**").hasRole("INSTRUCTOR");
            exchange.pathMatchers("/unihub/classroom/api/v1/classroom/join").hasAnyRole("INSTRUCTOR","STUDENT");
            exchange.pathMatchers("/unihub/classroom/api/v1/classroom/leave/**").hasAnyRole("INSTRUCTOR","STUDENT");
            exchange.pathMatchers("/unihub/classroom/api/v1/classroom/get-members/**").hasAnyRole("INSTRUCTOR","STUDENT");
            exchange.pathMatchers("/unihub/classroom/api/v1/classroom/get-owner/**").hasAnyRole("INSTRUCTOR","STUDENT");
            exchange.pathMatchers("/unihub/classroom/api/v1/classroom/get-enrolled-classes").hasAnyRole("INSTRUCTOR","STUDENT");
            exchange.pathMatchers("/unihub/classroom/api/v1/classroom/get-archived-classes").hasAnyRole("INSTRUCTOR","STUDENT");
            exchange.pathMatchers("/unihub/classroom/api/v1/material/instructor/**").hasRole("INSTRUCTOR");
            exchange.anyExchange().authenticated();
        });
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
                .addFilterAt(jwtValidatorFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }

    private boolean hasRequiredAuthorities(Authentication auth, String... requiredAuthorities) {
        List<String> authorities = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        for (String required : requiredAuthorities) {
            if (!authorities.contains(required)) {
                return false;
            }
        }
        return true;
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfigurationSource corsConfigurationSource;
        corsConfigurationSource = request -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
            corsConfiguration.setExposedHeaders(List.of("Authorization", "WWW-Authenticate"));
            corsConfiguration.setMaxAge(3600L);
            return corsConfiguration;
        };
        return corsConfigurationSource;
    }

}
