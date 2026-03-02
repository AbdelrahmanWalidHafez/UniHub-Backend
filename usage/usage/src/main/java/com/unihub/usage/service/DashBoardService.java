package com.unihub.usage.service;

import com.unihub.usage.client.AuthFeignClient;
import com.unihub.usage.client.UniversityFeignClient;
import com.unihub.usage.dto.response.CollegeDashboardDTO;
import com.unihub.usage.dto.response.DashBoardAggregatesDto;
import com.unihub.usage.dto.response.DashboardResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashBoardService implements IDashBoardService{
    @Value("${api.key}")
    private String apiKey;

    private final AuthFeignClient authFeignClient;

    private final UniversityFeignClient universityFeignClient;

    @Override
    public DashboardResponseDTO getDashBoard(HttpServletRequest servletRequest) {
        UUID tid=fetchUniversityIdFromHeader(servletRequest);
        DashBoardAggregatesDto userData = authFeignClient.getUserAnalysis(tid,apiKey).getBody();
        List<CollegeDashboardDTO> collegeData = universityFeignClient.getCollegeAnalysis(tid).getBody();
        assert collegeData != null;
        List<DashboardResponseDTO.CollegeUserDTO> usersPerCollege = collegeData.stream()
                .map(college -> {
                    assert userData != null;
                    return new DashboardResponseDTO.CollegeUserDTO(
                            college.getCollegeName(),
                            userData.getUsersPerCollege().getOrDefault(college.getCollegeId(), 0L) // zero if no users
                    );
                })
                .toList();
        assert userData != null;
        return new DashboardResponseDTO(
                userData.getTotalUsers(),
                collegeData.size(), // total colleges
                userData.getUsersByRole(),
                userData.getUsersByGender(),
                usersPerCollege
        );

    }

    private UUID fetchUniversityIdFromHeader(HttpServletRequest request){
        return  UUID.fromString(request.getHeader("X-User-University-Id"));
    }
}
