package com.unihub.auth.security.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        if (passwordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }
        throw new BadCredentialsException("Invalid username or password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
