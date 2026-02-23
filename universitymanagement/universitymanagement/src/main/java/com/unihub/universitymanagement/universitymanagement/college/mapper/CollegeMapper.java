package com.unihub.universitymanagement.universitymanagement.college.mapper;

import com.unihub.universitymanagement.universitymanagement.college.dto.request.CreateCollegeRequest;
import com.unihub.universitymanagement.universitymanagement.college.dto.response.CollegeDto;
import com.unihub.universitymanagement.universitymanagement.college.dto.response.CollegeMetadata;
import com.unihub.universitymanagement.universitymanagement.college.model.College;
import org.springframework.stereotype.Component;
@Component
public class CollegeMapper {

    public College toEntity(CreateCollegeRequest collegeRequest) {
    College college= new College();
    college.setCampus(collegeRequest.getCollegeCampus());
    college.setCollegeName(collegeRequest.getCollegeName());
     return college;
    }

    public CollegeDto toDto(College college) {
        CollegeDto collegeDto= new CollegeDto();
        collegeDto.setId(college.getId());
        collegeDto.setCollegeName(college.getCollegeName());
        collegeDto.setCampus(college.getCampus());
        collegeDto.setUpdatedAt(college.getUpdatedAt());
        collegeDto.setUpdatedBy(college.getUpdatedBy());
        collegeDto.setCreatedBy(college.getCreatedBy());
        collegeDto.setCreatedAt(college.getCreatedAt());
        return collegeDto;
    }

    public CollegeMetadata toMetaData(College college) {
        CollegeMetadata collegeMetadata= new CollegeMetadata();
        collegeMetadata.setId(college.getId());
        collegeMetadata.setCollegeName(college.getCollegeName());
        collegeMetadata.setCampus(college.getCampus());
        collegeMetadata.setCreatedAt(college.getCreatedAt());
        collegeMetadata.setCreatedBy(college.getCreatedBy());
        return collegeMetadata;
    }
}
