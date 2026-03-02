package com.unihub.auth.internal.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unihub.auth.security.dto.response.RoleDto;
import com.unihub.auth.security.dto.response.UniversityMetadataDto;
import com.unihub.auth.security.model.Gender;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemAdminResponse {

    private UUID uid;

    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("Date_of_birth")
    private LocalDate dob;

    private Gender gender;

    private String password;

    @JsonProperty("is_account_non_locked")
    boolean isAccountNonLocked;

    @JsonProperty("role")
    private RoleDto role;

    @JsonProperty("university_metadata")
    private UniversityMetadataDto universityMetadata;
}