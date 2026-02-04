package com.unihub.auth.security.mapper;

import com.unihub.auth.internal.mapper.UniversityMetaDataMapper;
import com.unihub.auth.security.dto.response.UserDto;
import com.unihub.auth.security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final RoleMapper roleMapper;

    private final UniversityMetaDataMapper universityMetaDataMapper;

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUid(user.getUid());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setDob(user.getDob());
        userDto.setGender(user.getGender());
        userDto.setRole(roleMapper.toDto(user.getRole()));
        if(user.getUniversityMetadata() != null) {
            userDto.setUniversityMetadata(universityMetaDataMapper.toDto(user.getUniversityMetadata()));
        }
        userDto.setCreatedBy(user.getCreatedBy());
        userDto.setUpdatedBy(user.getUpdatedBy());
        userDto.setUpdatedAt(user.getUpdatedAt());
        userDto.setCreatedAt(user.getCreatedAt());
        return userDto;
    }
}
