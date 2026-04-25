package com.unihub.auth.internal.mapper;

import com.unihub.auth.internal.dto.response.SystemAdminResponse;
import com.unihub.auth.security.mapper.RoleMapper;
import com.unihub.auth.security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SystemAdminMapper {
    private final UniversityMetaDataMapper universityMetaDataMapper;

    private final RoleMapper roleMapper;

    public SystemAdminResponse toDto(User user){
        SystemAdminResponse systemAdminResponse=new SystemAdminResponse();
        systemAdminResponse.setUid(user.getUid());
        systemAdminResponse.setEmail(user.getEmail());
        systemAdminResponse.setFirstName(user.getFirstName());
        systemAdminResponse.setLastName(user.getLastName());
        systemAdminResponse.setDob(user.getDob());
        systemAdminResponse.setGender(user.getGender());
        systemAdminResponse.setAccountNonLocked(user.isAccountNonLocked());
        systemAdminResponse.setRole(roleMapper.toDto(user.getRole()));
        systemAdminResponse.setUniversityMetadata(universityMetaDataMapper.toDto(user.getUniversityMetadata()));
        return systemAdminResponse;
    }
}
