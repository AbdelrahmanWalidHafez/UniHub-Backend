package com.unihub.auth.accountmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unihub.auth.security.model.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

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

    @JsonProperty("college_id")

    private UUID cid;
}
