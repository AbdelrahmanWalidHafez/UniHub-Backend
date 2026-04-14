package com.unihub.classroom.material.service;

import com.unihub.classroom.assginement.dto.request.CreateAssignmentRequest;
import com.unihub.classroom.assginement.dto.response.AssignmentResponseDto;
import com.unihub.classroom.material.dto.request.MaterialDto;
import com.unihub.classroom.material.dto.response.MaterialResponseDto;
import com.unihub.classroom.material.model.Material;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IMaterialService {

    MaterialResponseDto createAnnouncement(MaterialDto materialDto, List<MultipartFile> materialFiles, UUID cid, HttpServletRequest request) throws IOException;

    void deleteMaterial(UUID mid, HttpServletRequest request);

    MaterialResponseDto editMaterial( UUID mid, MaterialDto materialDto, List<MultipartFile> files, List<String> toDeleteFiles,HttpServletRequest request) throws IOException;

    MaterialResponseDto getMaterial(UUID mid, HttpServletRequest request);

    MaterialResponseDto createMaterial(MaterialDto materialDto, List<MultipartFile> materialFiles, UUID cid, HttpServletRequest request) throws IOException;

    AssignmentResponseDto createAssignment(CreateAssignmentRequest createAssignmentRequest, List<MultipartFile> materialFiles, UUID cid, HttpServletRequest request) throws IOException;

    Material generateMaterial(Material material, List<MultipartFile> materialFiles, UUID cid, HttpServletRequest request) throws IOException;

    AssignmentResponseDto editAssignment(UUID mid, CreateAssignmentRequest createAssignmentRequest, List<MultipartFile> materialFiles, List<String> toDeleteFiles, HttpServletRequest request) throws IOException;

    List<MaterialResponseDto> getAllMaterials(UUID id, HttpServletRequest request, int pageNum);

    List<MaterialResponseDto> getAllAssignments(UUID id, HttpServletRequest request, int pageNum);
}

