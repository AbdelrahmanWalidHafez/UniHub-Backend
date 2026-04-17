package com.unihub.ai.controller;

import com.unihub.ai.dto.MaterialMetaData;
import com.unihub.ai.service.IVectorStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/material")
public class MaterialController {

    private final IVectorStoreService vectorStoreService;

    @PostMapping(value = "/ingest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> upload(@RequestPart("file") MultipartFile file, @Valid @RequestPart("metadata")MaterialMetaData materialMetaData) throws IllegalAccessException {
        vectorStoreService.loadDoc(file.getResource(),metadata(materialMetaData));
        return ResponseEntity.accepted().build();
    }

    private Map<String, Object> metadata(MaterialMetaData dto) {
        Map<String, Object> map = new HashMap<>();
        map.put("materialId", dto.getMaterialId());
        map.put("classroomId", dto.getClassroomId());
        map.put("universityId", dto.getUniversityId());
        map.put("collegeId", dto.getCollegeId());
        map.put("materialType", dto.getMaterialType());
        map.put("materialHeadLine", dto.getHeadLine());
        map.put("materialDescription", dto.getDescription());
        map.put("classSubTitle", dto.getClassSubTitle());
        map.put("classTitle", dto.getClassTitle());
        return map;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id){
        vectorStoreService.deleteDoc(java.util.UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
