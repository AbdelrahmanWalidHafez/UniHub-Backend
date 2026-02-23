package com.unihub.gateway.security.config;

import com.unihub.gateway.security.filter.JwtValidatorFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
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
            exchange.pathMatchers("/unihub/universitymanagement/api/v1/get-university/**").authenticated();
            exchange.pathMatchers("/unihub/universitymanagement/api/v1/internal/**").denyAll();
            //S3 microservice
            exchange.pathMatchers("/unihub/s3/**").authenticated();
            exchange.anyExchange().authenticated();
        });
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
                .addFilterAt(jwtValidatorFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
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
