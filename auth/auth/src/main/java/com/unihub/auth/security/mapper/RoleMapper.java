package com.unihub.auth.security.mapper;

import com.unihub.auth.security.dto.response.RoleDto;
import com.unihub.auth.security.model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleDto toDto(Role role){
        RoleDto roleDto=new RoleDto();
        roleDto.setRid(role.getRid());
        roleDto.setName(role.getName());
        return roleDto;
    }
}
