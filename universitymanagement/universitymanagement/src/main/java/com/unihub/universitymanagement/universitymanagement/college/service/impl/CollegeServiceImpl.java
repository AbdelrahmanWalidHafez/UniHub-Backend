package com.unihub.universitymanagement.universitymanagement.college.service.impl;

import com.unihub.universitymanagement.universitymanagement.college.dto.request.CreateCollegeRequest;
import com.unihub.universitymanagement.universitymanagement.college.dto.response.CollegeDto;
import com.unihub.universitymanagement.universitymanagement.college.dto.response.CollegeMetadata;
import com.unihub.universitymanagement.universitymanagement.college.mapper.CollegeMapper;
import com.unihub.universitymanagement.universitymanagement.college.model.College;
import com.unihub.universitymanagement.universitymanagement.college.repository.CollegeRepository;
import com.unihub.universitymanagement.universitymanagement.college.service.ICollegeService;
import com.unihub.universitymanagement.universitymanagement.university.model.University;
import com.unihub.universitymanagement.universitymanagement.university.service.IUniversityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CollegeServiceImpl implements ICollegeService {

    private final CollegeMapper collegeMapper;

    private final CollegeRepository collegeRepository;

    private final IUniversityService universityService;

    @Override
    public CollegeDto createCollege(CreateCollegeRequest collegeRequest, HttpServletRequest request) {
        College college=collegeMapper.toEntity(collegeRequest);
        University university=universityService.fetchUniversity(fetchUniHeader(request));
        college.setUniversity(university);
        return collegeMapper.toDto(collegeRepository.save(college));
    }

    @Override
    public List<CollegeMetadata> getAllColleges(HttpServletRequest request, int pageNum, String sortDir,String sortField) {
        Pageable pageable=createPageable(pageNum,sortDir,sortField);
        University university=universityService.fetchUniversity(fetchUniHeader(request));
        return collegeRepository.findAllByUniversity(pageable,university)
                .stream()
                .map(collegeMapper::toMetaData)
                .toList();
    }


    @Override
    public CollegeDto getCollege(UUID uuid) {
        return collegeMapper.toDto(fetchCollege(uuid));
    }

    private College fetchCollege(UUID uuid){
        return collegeRepository.findById(uuid)
                .orElseThrow(()->new EntityNotFoundException("college with id "+uuid+" not found"));
    }

    private UUID fetchUniHeader(HttpServletRequest request){
        return  UUID.fromString(request.getHeader("X-User-University-Id"));
    }

    //TODO put it in common package across  the project
    private Pageable createPageable(int pageNum, String sortDir, String sortField){
        int pageSize=5;
        return  PageRequest.of(
                pageNum-1,
                pageSize,
                sortDir.equalsIgnoreCase("asc")? Sort.by(sortField).ascending():Sort.by(sortField).descending()
        );
    }

}


