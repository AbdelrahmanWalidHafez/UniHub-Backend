package com.unihub.classroom.material.service;

import com.unihub.classroom.material.dto.request.MaterialDto;
import com.unihub.classroom.material.dto.response.MaterialResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IMaterialService {

    MaterialResponseDto createMaterial(MaterialDto materialDto, List<MultipartFile> materialFiles, UUID cid, HttpServletRequest request) throws IOException;

    void deleteMaterial(UUID mid, HttpServletRequest request);

    MaterialResponseDto editMaterial( UUID mid, MaterialDto materialDto, List<MultipartFile> files, List<String> ToDeleteFiles,HttpServletRequest request) throws IOException;

    MaterialResponseDto getMaterial(UUID mid, HttpServletRequest request);
}

