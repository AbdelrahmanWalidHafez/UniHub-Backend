package com.unihub.auth.accountmanagement.strategy.impl;

import com.unihub.auth.accountmanagement.dto.request.BaseUserRequest;
import com.unihub.auth.accountmanagement.dto.request.InstructorUserRequest;
import com.unihub.auth.accountmanagement.strategy.UserCreationStrategy;
import com.unihub.auth.accountmanagement.strategy.UserRoles;
import com.unihub.auth.security.dto.response.UserDto;
import com.unihub.auth.security.mapper.UserMapper;
import com.unihub.auth.security.model.Role;
import com.unihub.auth.security.model.UniversityMetadata;
import com.unihub.auth.security.model.User;
import com.unihub.auth.security.repository.RoleRepository;
import com.unihub.auth.security.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InstructorCreationStrategy implements UserCreationStrategy {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Override
    public UserDto createUser(BaseUserRequest userRequest, Authentication authentication) {
        User user=userMapper.toEntity(userRequest);
        user.setRole(fetchRole(getSupportedRole().toString()));
        user.setUniversityMetadata(
                UniversityMetadata.builder()
                        .tid(fetchUniversityId(authentication))
                        .cid(((InstructorUserRequest) userRequest).getCid())
                        .user(user)
                        .build()
        );
        return userMapper.toDto(userRepository.save(user));
    }


    @Override
    public UserRoles getSupportedRole(){
        return UserRoles.ROLE_INSTRUCTOR;
    }

    @Override
    public Role fetchRole(String name){
        return roleRepository
                .findByName(name)
                .orElseThrow(()-> new EntityNotFoundException("No Role found with name: "+name));
    }
}
