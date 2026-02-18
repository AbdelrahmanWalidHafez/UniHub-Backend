package com.unihub.auth.internal.mapper;

import com.unihub.auth.security.dto.response.UniversityMetadataDto;
import com.unihub.auth.security.model.UniversityMetadata;
import org.springframework.stereotype.Component;

@Component
public class UniversityMetaDataMapper {
    public UniversityMetadataDto toDto(UniversityMetadata universityMetadata){
        UniversityMetadataDto universityMetadataDto=new UniversityMetadataDto();
        universityMetadataDto.setUid(universityMetadata.getUid());
        universityMetadataDto.setTid(universityMetadata.getTid());
        universityMetadataDto.setCid(universityMetadata.getCid());
        return universityMetadataDto;
    }
}