package com.unihub.auth.accountmanagement.job.processor;

import com.unihub.auth.accountmanagement.job.dto.UserCsvDto;
import com.unihub.auth.accountmanagement.strategy.UserRoles;
import com.unihub.auth.security.model.Role;
import com.unihub.auth.security.model.UniversityMetadata;
import com.unihub.auth.security.model.User;
import com.unihub.auth.security.repository.RoleRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.security.access.AccessDeniedException;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserProcessor implements ItemProcessor<UserCsvDto,User> {

    private final UUID tid;

    private Map<UserRoles, Role> roleHashMap;

    private final RoleRepository roleRepository;

    public UserProcessor(UUID tid, RoleRepository roleRepository) {
        this.tid = tid;
        this.roleRepository = roleRepository;
    }


    @Override
    public @Nullable User process(UserCsvDto item)  {
        if (roleHashMap == null) {
            roleHashMap = roleRepository.findByNameNot("ROLE_CUSTOMER_SERVICE").stream()
                    .collect(Collectors.toMap(
                            role -> UserRoles.valueOf(role.getName()),
                            role -> role
                    ));
        }
        return mapCsvToEntity(tid, item);
    }

    private User mapCsvToEntity(UUID tid, UserCsvDto dto) {
        CSVValidator.validate(dto);
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDob(dto.getDob());
        user.setGender(dto.getGender());
        user.setUniversityMetadata(
                UniversityMetadata.builder()
                        .tid(tid)
                        .user(user)
                        .build()
        );
        user.setRole(roleHashMap.get(dto.getRoleName()));
        user.setAccountNonLocked(false);
        switch (UserRoles.valueOf(user.getRole().getName())) {
            case UserRoles.ROLE_SYSTEM_ADMIN -> {
                return user;
            }
            case UserRoles.ROLE_INSTRUCTOR, UserRoles.ROLE_SECRETARY -> {
                user.getUniversityMetadata().setCid(dto.getCid());
                return user;
            }
            case UserRoles.ROLE_STUDENT -> {
                UniversityMetadata universityMetadata = user.getUniversityMetadata();
                universityMetadata.setCid(dto.getCid());
                universityMetadata.setGpa(dto.getGpa());
                return user;
            }
            default -> throw new IllegalArgumentException("Unknown role: " + dto.getRoleName().toString());
        }
    }

    private static class CSVValidator {
        private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z]{1,50}$");
        private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

         static void validate(UserCsvDto dto) {

            if (dto.getEmail() == null || !EMAIL_PATTERN.matcher(dto.getEmail()).matches()) {
                throw new IllegalArgumentException("Invalid email: " + dto.getEmail());
            }

            if (dto.getFirstName() == null || !NAME_PATTERN.matcher(dto.getFirstName()).matches()) {
                throw new IllegalArgumentException("Invalid first name: " + dto.getFirstName());
            }

            if (dto.getLastName() == null || !NAME_PATTERN.matcher(dto.getLastName()).matches()) {
                throw new IllegalArgumentException("Invalid last name: " + dto.getLastName());
            }

            if (dto.getDob() == null) {
                throw new IllegalArgumentException("Date of birth must not be null");
            }

            if (dto.getGender() == null) {
                throw new IllegalArgumentException("Gender must not be null");
            }

            if (dto.getRoleName() == null) {
                throw new IllegalArgumentException("Role must not be null");
            }

            switch (dto.getRoleName()) {
                case ROLE_STUDENT:
                    if (dto.getCid() == null || dto.getGpa() == null) {
                        throw new IllegalArgumentException("Student must have CID and GPA");
                    }
                    break;
                case ROLE_SECRETARY:
                case ROLE_INSTRUCTOR:
                    if (dto.getCid() == null) {
                        throw new IllegalArgumentException(dto.getRoleName() + " must have CID");
                    }
                    break;
                default:
            }

            if (dto.getGpa() != null && (dto.getGpa() < 0.0 || dto.getGpa() > 4.0)) {
                throw new IllegalArgumentException("GPA must be between 0.0 and 4.0: " + dto.getGpa());
            }
        }
    }
}
