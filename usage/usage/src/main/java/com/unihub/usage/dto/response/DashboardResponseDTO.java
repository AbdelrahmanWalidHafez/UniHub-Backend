package com.unihub.usage.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDTO {
    private long totalUsers;
    private long totalColleges;
    private Map<String, Long> usersByRole;
    private Map<String, Long> usersByGender;
    private List<CollegeUserDTO> usersPerCollege;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CollegeUserDTO {
        private String collegeName;
        private long userCount;
    }
}