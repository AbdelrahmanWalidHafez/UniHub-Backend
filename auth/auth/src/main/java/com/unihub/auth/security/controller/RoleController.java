package com.unihub.auth.security.controller;

import com.unihub.auth.security.dto.response.Roles;
import com.unihub.auth.security.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final IRoleService roleService;

    @GetMapping("/get-roles")
    public ResponseEntity<Roles> getRoles(){
        return ResponseEntity.ok(Roles.builder().roles(roleService.getRoles()).build());
    }
}
