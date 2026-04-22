package com.unihub.auth.accountmanagement.strategy;

import com.unihub.auth.accountmanagement.dto.request.BaseUserRequest;
import com.unihub.auth.security.dto.response.UserDto;
import com.unihub.auth.security.model.Role;
import org.springframework.security.core.Authentication;

import java.util.Objects;
import java.util.UUID;

public interface UserCreationStrategy{

    UserDto createUser(BaseUserRequest userRequest,Authentication authentication);

    UserRoles getSupportedRole();

    Role fetchRole(String name);

    default UUID fetchUniversityId(Authentication authentication){
        return UUID.fromString(Objects.requireNonNull(authentication.getDetails()).toString());
    }
}
