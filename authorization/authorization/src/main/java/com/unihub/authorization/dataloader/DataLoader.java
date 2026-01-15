package com.unihub.authorization.dataloader;

import com.unihub.authorization.security.model.Gender;
import com.unihub.authorization.security.model.Role;
import com.unihub.authorization.security.model.User;
import com.unihub.authorization.security.repository.RoleRepository;
import com.unihub.authorization.security.repository.UserRepository;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private User tempUser;

    @Override
    public void run(String... args) throws Exception {
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role role = Role.builder()
                            .name("USER")
                            .build();
                    return roleRepository.save(role);
                });
        tempUser = User.builder()
                .email("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .dob(LocalDate.of(1990, 1, 1))
                .gender(Gender.MALE)
                .password(passwordEncoder.encode("password123"))
                .isAccountNonLocked(true)
                .role(userRole)
                .build();
        tempUser = userRepository.save(tempUser);

    }
    @PreDestroy
    public void cleanup() {
        if (tempUser != null) {
            userRepository.delete(tempUser);
            System.out.println("Temporary user removed before shutdown");
        }
    }
}
