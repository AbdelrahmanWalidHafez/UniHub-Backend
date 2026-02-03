package com.unihub.auth.security.config;

import com.unihub.auth.security.filter.InternalApiKeyFilter;
import com.unihub.auth.security.filter.JwtValidatorFilter;
import com.unihub.auth.security.provider.ProjectAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ProjectSecurityConfig {

    private final JwtValidatorFilter  jwtValidatorFilter;

    private final InternalApiKeyFilter internalApiKeyFilter;

    @Bean
    DefaultSecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity)  {
        httpSecurity.sessionManagement((scm) -> scm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/auth/login","/api/v1/auth/refresh").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest()
                        .authenticated())
                .cors((corsConfig) -> corsConfig.configurationSource(corsConfigurationSource()))
                .addFilterBefore(internalApiKeyFilter,UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtValidatorFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        AuthenticationProvider authenticationProvider = new ProjectAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(Collections.singletonList(authenticationProvider));
        providerManager.setEraseCredentialsAfterAuthentication(true);
        return providerManager;
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfigurationSource corsConfigurationSource;
        corsConfigurationSource = request -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:3001"));
            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
            corsConfiguration.setExposedHeaders(List.of("Authorization","WWW-Authenticate"));
            corsConfiguration.setMaxAge(3600L);
            return corsConfiguration;
        };
        return corsConfigurationSource;
    }

}
