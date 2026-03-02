package com.unihub.auth.accountmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.unihub.auth.accountmanagement.strategy.UserRoles;
import com.unihub.auth.security.model.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "role_name",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StudentUserRequest.class, name = "ROLE_STUDENT"),
        @JsonSubTypes.Type(value = SecretaryUserRequest.class, name = "ROLE_SECRETARY"),
        @JsonSubTypes.Type(value = InstructorUserRequest.class, name = "ROLE_INSTRUCTOR"),
        @JsonSubTypes.Type(value = AdminUserRequest.class, name = "ROLE_SYSTEM_ADMIN")
})
public  class BaseUserRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @JsonProperty("first_name")
    @Pattern(
            regexp = "^\\p{L}+$",
            message = "First name must contain only letters"
    )
    @NotBlank(message = "First name is required")
    private String firstName;

    @JsonProperty("last_name")
    @Pattern(
            regexp = "^\\p{L}+$",
            message = "Last name must contain only letters"
    )
    @NotBlank(message = "Last name is required")
    private String lastName;


    @JsonProperty("date_of_birth")
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @JsonProperty("role_name")
    @NotNull(message = "Role is required")
    private UserRoles roleName;
}
