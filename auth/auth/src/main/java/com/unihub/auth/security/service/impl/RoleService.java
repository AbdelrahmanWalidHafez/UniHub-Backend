package com.unihub.auth.security.service.impl;

import com.unihub.auth.security.dto.response.RoleDto;
import com.unihub.auth.security.mapper.RoleMapper;
import com.unihub.auth.security.repository.RoleRepository;
import com.unihub.auth.security.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleMapper roleMapper;

    private final RoleRepository roleRepository;


    @Override
    public List<RoleDto> getRoles() {
        return  roleRepository.findByNameNot("ROLE_CUSTOMER_SERVICE")
                .stream()
                .map(roleMapper::toDto)
                .toList();
    }

}
