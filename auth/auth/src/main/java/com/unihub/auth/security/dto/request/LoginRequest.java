package com.unihub.auth.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message ="email can't be blank")
    private String email;

    @NotBlank(message ="password can't be blank")
    private String password;
}
