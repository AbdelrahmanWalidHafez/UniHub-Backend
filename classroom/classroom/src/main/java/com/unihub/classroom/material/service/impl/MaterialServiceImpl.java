package com.unihub.classroom.material.service.impl;

import com.unihub.classroom.clazz.model.ClassRoom;
import com.unihub.classroom.clazz.repository.ClassRoomRepository;
import com.unihub.classroom.clazz.service.IClassRoomService;
import com.unihub.classroom.material.client.S3FeignClient;
import com.unihub.classroom.material.dto.DeleteFileRequest;
import com.unihub.classroom.material.dto.MaterialDto;
import com.unihub.classroom.material.dto.MaterialResponseDto;
import com.unihub.classroom.material.dto.UploadFileRequest;
import com.unihub.classroom.material.mapper.MaterialMapper;
import com.unihub.classroom.material.model.Material;
import com.unihub.classroom.material.repository.MaterialRepository;
import com.unihub.classroom.material.service.IMaterialService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements IMaterialService {

    private final StreamBridge streamBridge;

    private final S3FeignClient s3FeignClient;

    private final MaterialMapper materialMapper;

    private final IClassRoomService classRoomService;

    private final MaterialRepository materialRepository;

    private final ClassRoomRepository classRoomRepository;

    @Value("${aws.bucket}")
    private String bucketLink;

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf",
            "image/jpeg",
            "image/png",
            "video/mp4",
            "video/quicktime",
            "video/x-msvideo",
            "video/webm",
            "video/x-matroska"
    );

    @Override
    @Transactional
    public MaterialResponseDto createMaterial(MaterialDto materialDto, List<MultipartFile> materialFiles, UUID cid, HttpServletRequest request) throws IOException {

       ClassRoom classroom=fetchClassRoom(request,cid);
        Material material=materialMapper.toEntity(materialDto);
        classroom.addMaterial(material);
        if(materialFiles!=null&&!materialFiles.isEmpty()){
          uploadFiles(materialFiles,classroom,material);
        }
        return materialMapper.toDto(materialRepository.save(material));
    }

    @Override
    @Transactional
    public void deleteMaterial(UUID mid, HttpServletRequest request) {
        Material material=fetchMaterial(mid,request);
        ClassRoom classRoom=material.getClassroom();
        if (material.getMaterialUrls() != null) {
            material.getMaterialUrls().forEach(this::deleteFile);
        }
        classRoom.removeMaterial(material);
        materialRepository.delete(material);
    }

    @Override
    @Transactional
    public MaterialResponseDto editMaterial(UUID mid, MaterialDto materialDto, List<MultipartFile> files, List<String> ToDeleteFiles,HttpServletRequest request) throws IOException {
        Material material=fetchMaterial(mid,request);
        editMaterial(materialDto,material);
        if(files!=null&&!files.isEmpty()){
            uploadFiles(files,material.getClassroom(),material);
        }
        if(ToDeleteFiles!=null&&!ToDeleteFiles.isEmpty()){
            ToDeleteFiles.forEach(this::deleteFile);
            material.getMaterialUrls().removeAll(ToDeleteFiles);
        }
        return materialMapper.toDto(materialRepository.save(material));
    }


    @Override
    @Transactional
    public MaterialResponseDto getMaterial(UUID mid, HttpServletRequest request) {
        return materialMapper.toDto(
                materialRepository
                        .findMaterial(mid, classRoomService.fetchEmailFromHeader(request))
                        .orElseThrow(()->new EntityNotFoundException("Material not found with id: "+mid)
                        )
        );
    }



    private void uploadFiles(List<MultipartFile> materialFiles,ClassRoom classRoom,Material material) throws IOException {
        for (MultipartFile file : materialFiles) {
            if (isInvalidValidContentType(file)) {
                log.warn("Invalid or unsupported content type: {}", file.getContentType());
                continue;
            }
            String key=generateKey(file,classRoom);
            material.getMaterialUrls().add(bucketLink+key);
            UploadFileRequest uploadFileRequest= UploadFileRequest.builder()
                    .fileContent(file.getBytes())
                    .key(key)
                    .contentType(file.getContentType())
                    .build();
            uploadFile(uploadFileRequest);
        }
    }


    private ClassRoom fetchClassRoom(HttpServletRequest request,UUID id){
        return classRoomRepository
                .findByCreatedByAndId(classRoomService.fetchEmailFromHeader(request),id )
                .orElseThrow(()->new EntityNotFoundException("Classroom not found with id: "+id));
    }

    private  boolean isInvalidValidContentType(MultipartFile file) {
        String contentType = file.getContentType();
        return !ALLOWED_CONTENT_TYPES.contains(contentType);
    }

    private String generateKey(MultipartFile file,ClassRoom classRoom){
        String extension = getExtension(file.getOriginalFilename());
        return   classRoom.getCode() + "/" + UUID.randomUUID() + "." + extension;
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "bin";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    private void uploadFile(UploadFileRequest uploadFileRequest){
        s3FeignClient.uploadFile(uploadFileRequest);
    }

    private Material fetchMaterial(UUID mid,HttpServletRequest request){
        return materialRepository.findByMidAndCreatedBy(mid, classRoomService.fetchEmailFromHeader(request))
                .orElseThrow(()->new EntityNotFoundException("Material not found with id: "+mid));
    }

    private void deleteFile(String key) {
        if (key != null) {
            streamBridge.send("deleteFile-out-0", DeleteFileRequest.builder().key(key.replace(bucketLink,"")).build());
        }
    }

    private void editMaterial(MaterialDto materialDto,Material material){
        material.setMaterialType(materialDto.getMaterialType());
        material.setDescription(materialDto.getDescription());
        material.setHeadLine(materialDto.getHeadLine());
    }
}
