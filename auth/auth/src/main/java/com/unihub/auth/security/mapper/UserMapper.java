package com.unihub.auth.security.mapper;

import com.unihub.auth.accountmanagement.dto.request.BaseUserRequest;
import com.unihub.auth.accountmanagement.dto.response.UserMetaDataResponse;
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

    public User toEntity(BaseUserRequest userRequest){
        User user=new User();
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setDob(userRequest.getDob());
        user.setGender(userRequest.getGender());
        user.setAccountNonLocked(false);
        return  user;
    }

    public UserMetaDataResponse toMetadata(User user){
        UserMetaDataResponse metaDataResponse=new UserMetaDataResponse();
        metaDataResponse.setUid(user.getUid());
        metaDataResponse.setEmail(user.getEmail());
        return metaDataResponse;
    }

}
