package com.unihub.universitymanagement.universitymanagement.college.service;

import com.unihub.universitymanagement.universitymanagement.college.dto.request.CreateCollegeRequest;
import com.unihub.universitymanagement.universitymanagement.college.dto.response.CollegeDto;

import java.util.UUID;

public interface ICollegeService {
    CollegeDto createCollege(CreateCollegeRequest collegeRequest);
    CollegeDto getCollege(UUID uuid);

}
