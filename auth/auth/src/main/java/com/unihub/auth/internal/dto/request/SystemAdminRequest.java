package com.unihub.auth.internal.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class SystemAdminRequest {

    @NotNull(message = "The Tenant ID is required")
    private UUID tid;

    @Pattern(
            regexp = "^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Invalid university domain"
    )
    @NotBlank(message = "University domain cannot be empty")
    @JsonProperty("university_domain")
    private String universityDomain;
}
