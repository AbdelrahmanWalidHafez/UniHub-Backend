package com.unihub.universitymanagement.universitymanagement.college.service.impl;

import com.unihub.universitymanagement.universitymanagement.college.dto.request.CreateCollegeRequest;
import com.unihub.universitymanagement.universitymanagement.college.dto.response.CollegeDto;
import com.unihub.universitymanagement.universitymanagement.college.mapper.CollegeMapper;
import com.unihub.universitymanagement.universitymanagement.college.model.College;
import com.unihub.universitymanagement.universitymanagement.college.repository.CollegeRepository;
import com.unihub.universitymanagement.universitymanagement.college.service.ICollegeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CollegeServiceImpl implements ICollegeService {

    private final CollegeMapper collegeMapper;

    private final CollegeRepository collegeRepository;

    @Override
    public CollegeDto createCollege(CreateCollegeRequest collegeRequest) {
        College college=collegeMapper.toEntity(collegeRequest);
        return collegeMapper.toDto(collegeRepository.save(college));
    }

    @Override
    public CollegeDto getCollege(UUID uuid) {
        return collegeMapper.toDto(fetchCollege(uuid));
        }

    private College fetchCollege(UUID uuid){
        return collegeRepository.findById(uuid)
                .orElseThrow(()->new EntityNotFoundException("college with id "+uuid+" not found"));
    }

    }


