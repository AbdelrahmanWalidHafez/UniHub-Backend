package com.unihub.ai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unihub.ai.dto.MaterialMetaData;
import com.unihub.ai.service.IVectorStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/material")
public class MaterialController {

    private final ObjectMapper objectMapper;

    private final IVectorStoreService vectorStoreService;

    @PostMapping(value = "/ingest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> upload(@RequestPart("file") MultipartFile file, @Valid @RequestPart("metadata")String metadataJson) throws IOException {
        vectorStoreService.loadDoc(new ByteArrayResource(file.getBytes()), metadata(objectMapper.readValue(metadataJson, MaterialMetaData.class)));
        return ResponseEntity.accepted().build();
    }

    private Map<String, Object> metadata(MaterialMetaData dto) {
        Map<String, Object> map = new HashMap<>();
        map.put("materialId", String.valueOf(dto.getMaterialId()));
        map.put("classroomId", String.valueOf(dto.getClassroomId()));
        map.put("universityId", String.valueOf(dto.getUniversityId()));
        map.put("collegeId", String.valueOf(dto.getCollegeId()));
        map.put("materialType", String.valueOf(dto.getMaterialType()));
        map.put("materialHeadLine", dto.getHeadLine());
        map.put("materialDescription", dto.getDescription());
        map.put("classSubTitle", dto.getClassSubTitle());
        map.put("classTitle", dto.getClassTitle());
        log.debug(map.toString());
        return map;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id){
        vectorStoreService.deleteDoc(id);
        return ResponseEntity.noContent().build();
    }
}
