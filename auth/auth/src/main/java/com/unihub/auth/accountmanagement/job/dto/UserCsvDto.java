package com.unihub.auth.accountmanagement.job.dto;

import com.unihub.auth.accountmanagement.strategy.UserRoles;
import com.unihub.auth.security.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCsvDto {

    private String email;

    private String firstName;

    private String lastName;

    private LocalDate dob;

    private Gender gender;

    private UserRoles roleName;

    private UUID cid;

    private Double gpa;

}

