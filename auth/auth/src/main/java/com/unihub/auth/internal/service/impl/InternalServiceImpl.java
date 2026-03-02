package com.unihub.auth.internal.service.impl;

import com.unihub.auth.internal.dto.request.SystemAdminRequest;
import com.unihub.auth.internal.dto.response.DashBoardAggregatesDto;
import com.unihub.auth.internal.dto.response.SystemAdminResponse;
import com.unihub.auth.internal.mapper.SystemAdminMapper;
import com.unihub.auth.internal.service.IInternalService;
import com.unihub.auth.security.model.Gender;
import com.unihub.auth.security.model.Role;
import com.unihub.auth.security.model.UniversityMetadata;
import com.unihub.auth.security.model.User;
import com.unihub.auth.security.repository.RoleRepository;
import com.unihub.auth.security.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class InternalServiceImpl implements IInternalService {

    private final UserRepository userRepository;

    private final RoleRepository rolerepository;

    private final PasswordEncoder passwordEncoder;

    private final SystemAdminMapper systemAdminMapper;

    @Override
    @Transactional
    public SystemAdminResponse createSystemAdmin(SystemAdminRequest request) {
        User user = createUser(request);
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
        SystemAdminResponse systemAdminResponse = systemAdminMapper.toDto(user);
        systemAdminResponse.setPassword(rawPassword);
        return systemAdminResponse;
    }

    @Override
    public Long countUsers(UUID cid) {
        return userRepository.countByUniversityCid(cid);
    }

    @Override
    @Transactional(readOnly = true)
    public DashBoardAggregatesDto analysis(UUID tid)  {
        long totalUsers = userRepository.countByUniversityTid(tid);
        Map<String, Long> usersByRole = userRepository.countUsersByRole(tid)
                .stream()
                .collect(Collectors.toMap(
                        r -> (String) r[0],
                        r -> ((Number) r[1]).longValue()
                ));
        Map<String, Long> usersByGender = userRepository.countUsersByGender(tid)
                .stream()
                .collect(Collectors.toMap(
                        g -> g[0] != null ? g[0].toString() : "UNKNOWN",
                        g -> ((Number) g[1]).longValue()
                ));
        List<Object[]> collegeCounts = userRepository.countUsersByCollege(tid);
        Map<UUID, Long> usersPerCollege = collegeCounts.stream()
                .filter(c -> c[0] != null)
                .collect(Collectors.toMap(
                        c -> UUID.fromString(c[0].toString()),
                        c -> ((Number) c[1]).longValue()
                ));

        return new DashBoardAggregatesDto(
                totalUsers,
                usersByRole,
                usersByGender,
                usersPerCollege
        );
    }

    private User createUser(SystemAdminRequest request) {
        User user = User.builder()
                .email(createEmail(request))
                .firstName("admin")
                .lastName("admin")
                .dob(LocalDate.now())
                .gender(Gender.OTHER)
                .isAccountNonLocked(true)
                .password(createPassword())
                .role(fetchRole())
                .build();
        UniversityMetadata metadata = UniversityMetadata.builder()
                .tid(request.getTid())
                .user(user)
                .build();
        user.setUniversityMetadata(metadata);
        return user;
    }

    private String createEmail(SystemAdminRequest request) {
        return "system_admin@" + request.getUniversityDomain();
    }

    private String createPassword() {
        StringBuilder password = new StringBuilder(8);
        Random random = new SecureRandom();
        while (password.length() < 8) {

            char ch = (char) (random.nextInt(126 - 33 + 1) + 33);

            if (Character.isLetterOrDigit(ch) || "!@#$%^&*()-_+=<>?".indexOf(ch) >= 0) {
                password.append(ch);
            }
        }

        return password.toString();
    }

    private Role fetchRole() {
        return rolerepository.findByName("ROLE_SYSTEM_ADMIN")
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

}

