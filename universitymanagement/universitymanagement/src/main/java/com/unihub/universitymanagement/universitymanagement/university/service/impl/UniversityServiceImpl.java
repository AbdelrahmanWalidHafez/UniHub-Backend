package com.unihub.universitymanagement.universitymanagement.university.service.impl;

import com.unihub.universitymanagement.universitymanagement.university.dto.response.UniversityMetaData;
import com.unihub.universitymanagement.universitymanagement.university.dto.response.UniversityMetadataResponses;
import com.unihub.universitymanagement.universitymanagement.university.dto.response.UniversityResponse;
import com.unihub.universitymanagement.universitymanagement.university.mapper.UniversityMapper;
import com.unihub.universitymanagement.universitymanagement.university.model.University;
import com.unihub.universitymanagement.universitymanagement.university.repository.UniversityRepository;
import com.unihub.universitymanagement.universitymanagement.university.service.IUniversityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements IUniversityService {

    private final UniversityMapper  universityMapper;

    private final UniversityRepository universityRepository;

    @Override
    public UniversityResponse getUniversity(UUID universityId) {
        return universityMapper.toDto(fetchUniversity(universityId));
    }

    @Override
    public List<UniversityMetaData> getUniversityMetaDataList(int pageNum, String sortDir,String sortField) {
        Pageable pageable=createPageable(pageNum,sortDir,sortField);
        return universityRepository
                .findAll(pageable)
                .stream()
                .map(universityMapper::toMetaData).toList();
    }

    @Override
    public UniversityMetadataResponses searchUniversity(String searchText) {
        return UniversityMetadataResponses.builder()
                .universityMetaDataList(
                        universityRepository.searchUniversityNames(searchText).stream().map(universityMapper::toMetaData).toList()
                ).build();
    }

    @Override
    public University fetchUniversity(UUID universityId){
        return universityRepository.findById(universityId)
                .orElseThrow(()->new EntityNotFoundException("University with id "+universityId+" not found"));
    }

    private Pageable createPageable(int pageNum, String sortDir, String sortField){

        int pageSize=10;
        return  PageRequest.of(
                pageNum-1,
                pageSize,
                sortDir.equalsIgnoreCase("asc")? Sort.by(sortField).ascending():Sort.by(sortField).descending()
        );
    }

}
