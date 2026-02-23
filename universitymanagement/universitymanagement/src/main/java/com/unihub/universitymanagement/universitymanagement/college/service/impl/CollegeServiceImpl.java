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
import jakarta.persistence.LockModeType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CollegeServiceImpl implements ICollegeService {

    private final CollegeMapper collegeMapper;

    private final CollegeRepository collegeRepository;

    private final IUniversityService universityService;

    @Override
    @Transactional()
    public CollegeDto createCollege(CreateCollegeRequest collegeRequest, HttpServletRequest request) {
        College college=collegeMapper.toEntity(collegeRequest);
        University university=universityService.fetchUniversity(fetchUniHeader(request));
        college.setUniversity(university);
        return collegeMapper.toDto(collegeRepository.save(college));
    }

    @Override
    public CollegeDto getCollege(UUID uuid,HttpServletRequest request) {
        return collegeMapper.toDto(fetchCollege(uuid,request));
    }

    @Override
    public List<CollegeMetadata> getAllColleges(HttpServletRequest request, int pageNum, String sortDir,String sortField) {
        Pageable pageable=createPageable(pageNum,sortDir,sortField);
        return collegeRepository.findAllByUniversity_UniId(pageable,fetchUniHeader(request))
                .stream()
                .map(collegeMapper::toMetaData)
                .toList();
    }


    @Transactional
    @Lock(LockModeType.OPTIMISTIC)
    public CollegeDto updateCollege(HttpServletRequest request,UUID uuid,CreateCollegeRequest createCollegeRequest){
        College college=fetchCollege(uuid,request);
        college.setCollegeName(createCollegeRequest.getCollegeName());
        college.setCampus(createCollegeRequest.getCollegeCampus());
        return collegeMapper.toDto(collegeRepository.save(college));
    }

    @Transactional
    @Lock(LockModeType.OPTIMISTIC)
    public void deleteCollege(HttpServletRequest request, UUID uuid){
        College college=fetchCollege(uuid,request);
        collegeRepository.delete(college);
    }

    private College fetchCollege(UUID uuid,HttpServletRequest request){
        return collegeRepository.findByIdAndUniversity_UniId(uuid,fetchUniHeader(request))
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


