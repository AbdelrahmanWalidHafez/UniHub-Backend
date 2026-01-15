package com.unihub.authorization.security.service;

import com.unihub.authorization.security.model.User;
import com.unihub.authorization.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProjectUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, AccountStatusException {
        User user=userRepository
                .findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid Credential."));
        if (!user.isAccountNonLocked()) {
           throw new LockedException("Your Account is Locked. Please contact your administrator to unlock it.");
        }
        List<GrantedAuthority> roles= Stream.of(user.getRole()).map(role ->(GrantedAuthority) role::getName).toList();
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),roles);
    }

    @Override
    public void createUser(UserDetails user) {
        //TODO
    }

    @Override
    public void updateUser(UserDetails user) {
        //TODO
    }

    @Override
    public void deleteUser(String username) {
        //TODO
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        //TODO
    }

    @Override
    public boolean userExists(String username) {
        return false;
    }
}
