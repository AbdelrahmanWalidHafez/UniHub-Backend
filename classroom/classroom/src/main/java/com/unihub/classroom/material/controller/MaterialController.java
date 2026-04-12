package com.unihub.classroom.material.controller;

import com.unihub.classroom.material.dto.request.MaterialDto;
import com.unihub.classroom.material.dto.response.MaterialResponseDto;
import com.unihub.classroom.material.service.IMaterialService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/material")
public class MaterialController {

    private final IMaterialService materialService;

    @PostMapping(value = "/instructor/create/{id}",consumes =MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MaterialResponseDto> createMaterial(@RequestPart("data") @Valid MaterialDto materialDto,
                                                              @RequestPart(value = "files",required = false) List<MultipartFile> files,
                                                              @PathVariable UUID id,
                                                              HttpServletRequest request) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(materialService.createMaterial(materialDto,files,id,request));
    }

    @DeleteMapping("/instructor/delete/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable UUID id, HttpServletRequest request){
        materialService.deleteMaterial(id,request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/instructor/edit/{id}")
    public ResponseEntity<MaterialResponseDto> editMaterial(@PathVariable UUID id,
                                                            @RequestPart("data") @Valid MaterialDto materialDto,
                                                            @RequestPart(value = "files",required = false) List<MultipartFile> files,
                                                            @RequestPart(value = "ToDeleteFiles",required = false) List<String> ToDeleteFiles,
                                                            HttpServletRequest request) throws IOException {
        return ResponseEntity.ok(materialService.editMaterial(id, materialDto, files, ToDeleteFiles, request));

    }

    @GetMapping("/get-material/{id}")
    public ResponseEntity<MaterialResponseDto> getMaterial(@PathVariable UUID id, HttpServletRequest request){
        return ResponseEntity.ok(materialService.getMaterial(id,request));
    }
}
