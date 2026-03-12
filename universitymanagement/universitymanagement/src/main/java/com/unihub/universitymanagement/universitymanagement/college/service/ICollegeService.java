package com.unihub.universitymanagement.universitymanagement.college.service;

import com.unihub.universitymanagement.universitymanagement.college.dto.request.CreateCollegeRequest;
import com.unihub.universitymanagement.universitymanagement.college.dto.response.CollegeDto;
import com.unihub.universitymanagement.universitymanagement.college.dto.response.CollegeMetadata;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface ICollegeService {

    CollegeDto createCollege(CreateCollegeRequest collegeRequest, HttpServletRequest request);

    List<CollegeMetadata> getAllColleges(HttpServletRequest request, int pageNum, String sortDir, String sortField);

    CollegeDto getCollege(UUID uuid,HttpServletRequest request);

    CollegeDto updateCollege(HttpServletRequest request,UUID uuid,CreateCollegeRequest createCollegeRequest);

    void deleteCollege(HttpServletRequest request,UUID uuid);

    List<CollegeMetadata> searchColleges(String searchText,HttpServletRequest request);

}
