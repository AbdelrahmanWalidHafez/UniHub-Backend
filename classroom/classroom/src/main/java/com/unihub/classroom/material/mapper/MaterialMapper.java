package com.unihub.classroom.material.mapper;

import com.unihub.classroom.material.dto.request.MaterialDto;
import com.unihub.classroom.material.dto.response.MaterialResponseDto;
import com.unihub.classroom.material.model.Material;
import org.springframework.stereotype.Component;

@Component
public class MaterialMapper {

    public Material toEntity(MaterialDto materialDto){
        Material material = new Material();
        material.setHeadLine(materialDto.getHeadLine());
        material.setDescription(materialDto.getDescription());
        material.setCommentsCount(0);
        return material;
    }

    public MaterialResponseDto toDto(Material material){
        MaterialResponseDto materialResponseDto = new MaterialResponseDto();
        materialResponseDto.setMid(material.getMid());
        materialResponseDto.setHeadLine(material.getHeadLine());
        materialResponseDto.setDescription(material.getDescription());
        materialResponseDto.setMaterialType(material.getMaterialType());
        materialResponseDto.setMaterialUrls(material.getMaterialUrls());
        materialResponseDto.setCreatedBy(material.getCreatedBy());
        materialResponseDto.setUpdatedBy(material.getUpdatedBy());
        materialResponseDto.setCreatedAt(material.getCreatedAt());
        materialResponseDto.setUpdatedAt(material.getUpdatedAt());
        materialResponseDto.setCommentsCount(material.getCommentsCount());
        return materialResponseDto;
    }
}
