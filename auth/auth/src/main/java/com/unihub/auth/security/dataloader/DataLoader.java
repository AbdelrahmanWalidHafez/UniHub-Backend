package com.unihub.auth.security.dataloader;

import com.unihub.auth.security.model.Gender;
import com.unihub.auth.security.model.Role;
import com.unihub.auth.security.model.User;
import com.unihub.auth.security.repository.RoleRepository;
import com.unihub.auth.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;


    @Override
    public void run(String... args) throws Exception {
        var role1 = roleRepository.findByName("ROLE_CUSTOMER_SERVICE").orElseGet(()->roleRepository.save(Role.builder().name("ROLE_CUSTOMER_SERVICE").build()));
        var role2 = roleRepository.findByName("ROLE_SYSTEM_ADMIN").orElseGet(()->roleRepository.save(Role.builder().name("ROLE_SYSTEM_ADMIN").build()));
        if(userRepository.findByEmail("customer_service@example.com").isEmpty()){
            var user= User.builder()
                    .firstName("firstName")
                    .lastName("lastName")
                    .password("{noop}password")
                    .email("customer_service@example.com")
                    .dob(LocalDate.of(1990, 1, 1))
                    .gender(Gender.OTHER)
                    .isAccountNonLocked(true)
                    .role(role1)
                    .build();
            userRepository.save(user);
        }
        if(userRepository.findByEmail("walidlrahmn5@gmail.com").isEmpty()){
            var user= User.builder()
                    .firstName("firstName")
                    .lastName("lastName")
                    .password("{noop}password")
                    .email("walidlrahmn5@gmail.com")
                    .dob(LocalDate.of(2003, 11, 11))
                    .gender(Gender.MALE)
                    .isAccountNonLocked(true)
                    .role(role1)
                    .build();
            userRepository.save(user);
        }
    }
}
