package com.unihub.auth.internal.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashBoardAggregatesDto {

    @JsonProperty("total_users")
    private long totalUsers;

    @JsonProperty("users_by_role")
    private Map<String, Long> usersByRole;

    @JsonProperty("users_by_gender")
    private Map<String, Long> usersByGender;

    @JsonProperty("users_per_college")
    private Map<UUID, Long> usersPerCollege;
}