package com.unihub.auth.security.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Roles {

    private List<RoleDto> roles;
}
