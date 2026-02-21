package com.unihub.auth.security.service.impl;

import com.unihub.auth.security.dto.response.UserDto;
import com.unihub.auth.security.mapper.UserMapper;
import com.unihub.auth.security.model.User;
import com.unihub.auth.security.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProjectUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("invalid username or password"));
        if (!user.isAccountNonLocked() && !(user.getEmail() == null)) {
          throw new LockedException("Account is locked contact your administrator");
        }
        List<GrantedAuthority> authorities = Stream.of(user.getRole()).map(role -> (GrantedAuthority) role::getName).toList();
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public boolean isExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDto getUserInfo(Authentication authentication) {
        return userMapper.toDto(fetchUser(authentication.getName()));
    }

    private User fetchUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()->new EntityNotFoundException("user not found"));
    }


}
