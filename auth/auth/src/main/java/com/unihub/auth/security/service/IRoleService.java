package com.unihub.auth.security.service;

import com.unihub.auth.security.dto.response.RoleDto;

import java.util.List;

public interface IRoleService {

    List<RoleDto> getRoles();

}
