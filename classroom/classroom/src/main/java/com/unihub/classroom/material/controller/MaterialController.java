package com.unihub.classroom.material.controller;

import com.unihub.classroom.assginement.dto.request.CreateAssignmentRequest;
import com.unihub.classroom.assginement.dto.response.AssignmentResponseDto;
import com.unihub.classroom.material.dto.request.MaterialDto;
import com.unihub.classroom.material.dto.response.MaterialResponseDto;
import com.unihub.classroom.material.dto.response.MaterialResponsesDto;
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

    @PostMapping(value = "/instructor/create-announcement/{id}",consumes =MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MaterialResponseDto> createAnnouncement(@RequestPart("data") @Valid MaterialDto materialDto,
                                                              @RequestPart(value = "files",required = false) List<MultipartFile> files,
                                                              @PathVariable UUID id,
                                                              HttpServletRequest request) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(materialService.createAnnouncement(materialDto,files,id,request));
    }

    @PostMapping(value = "/instructor/create-material/{id}",consumes =MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MaterialResponseDto> createMaterial(@RequestPart("data") @Valid MaterialDto materialDto,
                                                              @RequestPart(value = "files",required = false) List<MultipartFile> files,
                                                              @PathVariable UUID id,
                                                              HttpServletRequest request) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(materialService.createMaterial(materialDto,files,id,request));
    }

    @PostMapping(value = "/instructor/create-assignment/{id}",consumes =MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AssignmentResponseDto> createAssignment(@RequestPart("data") @Valid CreateAssignmentRequest createAssignmentRequest,
                                                                  @RequestPart(value = "files",required = false) List<MultipartFile> files,
                                                                  @PathVariable UUID id,
                                                                  HttpServletRequest request) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(materialService.createAssignment(createAssignmentRequest,files,id,request));
    }

    @DeleteMapping("/instructor/delete/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable UUID id, HttpServletRequest request){
        materialService.deleteMaterial(id,request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value="/instructor/edit/{id}",consumes =MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MaterialResponseDto> editMaterial(@PathVariable UUID id,
                                                            @RequestPart("data") @Valid MaterialDto materialDto,
                                                            @RequestPart(value = "files",required = false) List<MultipartFile> files,
                                                            @RequestPart(value = "ToDeleteFiles",required = false) List<String> toDeleteFiles,
                                                            HttpServletRequest request) throws IOException {
        return ResponseEntity.ok(materialService.editMaterial(id, materialDto, files, toDeleteFiles, request));

    }

    @PutMapping("/instructor/edit/assignment/{id}")
    public ResponseEntity<AssignmentResponseDto> editAssignment(@PathVariable UUID id, @RequestPart("data") @Valid CreateAssignmentRequest createAssignmentRequest,
                                                                @RequestPart(value = "files",required = false) List<MultipartFile> files,
                                                                @RequestPart(value = "ToDeleteFiles",required = false) List<String> toDeleteFiles,
                                                                HttpServletRequest request) throws IOException {
        return ResponseEntity.ok(materialService.editAssignment(id,createAssignmentRequest,files,toDeleteFiles,request));

    }



    @GetMapping("/get-all-materials/{id}")
    public ResponseEntity<MaterialResponsesDto> getAllMaterials(@PathVariable UUID id, HttpServletRequest request, @RequestParam(value = "page_num",defaultValue = "1")int pageNum){
        return ResponseEntity.ok(MaterialResponsesDto.builder().materials(materialService.getAllMaterials(id,request,pageNum)).build());
    }

    @GetMapping("/get-all-assignments/{id}")
    public ResponseEntity<MaterialResponsesDto> getAllAssignments(@PathVariable UUID id, HttpServletRequest request, @RequestParam(value = "page_num",defaultValue = "1")int pageNum){
        return ResponseEntity.ok(MaterialResponsesDto.builder().materials(materialService.getAllAssignments(id,request,pageNum)).build());
    }

}
