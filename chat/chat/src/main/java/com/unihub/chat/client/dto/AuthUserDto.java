package com.unihub.chat.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AuthUserDto {

    @JsonProperty("user_id")
    private UUID userId;

    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private RoleDto role;

    @JsonProperty("university")
    private UniversityDto universityMetadata;

    @Getter
    @Setter
    public static class RoleDto {
        private String name;
    }

    @Getter
    @Setter
    public static class UniversityDto {
        private UUID tid;
        private UUID cid;
    }
}