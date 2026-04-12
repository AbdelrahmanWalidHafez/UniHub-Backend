package com.unihub.classroom.material.service.impl;

import com.unihub.classroom.assginement.dto.request.CreateAssignmentRequest;
import com.unihub.classroom.assginement.dto.response.AssignmentResponseDto;
import com.unihub.classroom.assginement.mapper.AssignmentMapper;
import com.unihub.classroom.assginement.service.IAssignmentService;
import com.unihub.classroom.clazz.model.ClassRoom;
import com.unihub.classroom.clazz.repository.ClassRoomRepository;
import com.unihub.classroom.clazz.service.IClassRoomService;
import com.unihub.classroom.material.client.S3FeignClient;
import com.unihub.classroom.material.dto.request.DeleteFileRequest;
import com.unihub.classroom.material.dto.request.MaterialDto;
import com.unihub.classroom.material.dto.request.UploadFileRequest;
import com.unihub.classroom.material.dto.response.MaterialResponseDto;
import com.unihub.classroom.material.mapper.MaterialMapper;
import com.unihub.classroom.material.model.Material;
import com.unihub.classroom.material.model.enums.MaterialType;
import com.unihub.classroom.material.repository.MaterialRepository;
import com.unihub.classroom.material.service.IMaterialService;
import com.unihub.classroom.material.template.MaterialMaker;
import com.unihub.classroom.material.template.concrete.AnnouncementMaker;
import com.unihub.classroom.material.template.concrete.AssignmentMaker;
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
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements IMaterialService {

    private final StreamBridge streamBridge;

    private final S3FeignClient s3FeignClient;

    private final MaterialMapper materialMapper;

    private final AssignmentMapper assignmentMapper;

    private final IClassRoomService classRoomService;

    private final IAssignmentService assignmentService;

    private final MaterialRepository materialRepository;

    private final ClassRoomRepository classRoomRepository;

    @Value("${aws.bucket}")
    private String bucketLink;

    @Override
    @Transactional
    public MaterialResponseDto createAnnouncement(MaterialDto materialDto, List<MultipartFile> materialFiles, UUID cid, HttpServletRequest request) throws IOException {
        MaterialMaker materialMaker= new AnnouncementMaker(this);
        return materialMapper.toDto(generateMaterial(materialDto,materialMaker,materialFiles,cid,request));
    }


    @Override
    @Transactional
    public MaterialResponseDto createMaterial(MaterialDto materialDto, List<MultipartFile> materialFiles, UUID cid, HttpServletRequest request) throws IOException {
        MaterialMaker materialMaker=new com.unihub.classroom.material.template.concrete.MaterialMaker(this);
        return materialMapper.toDto(generateMaterial(materialDto,materialMaker,materialFiles,cid,request));
    }


    @Override
    @Transactional
    public AssignmentResponseDto createAssignment(CreateAssignmentRequest createAssignmentRequest, List<MultipartFile> materialFiles, UUID cid, HttpServletRequest request) throws IOException {
       MaterialMaker materialMaker= new AssignmentMaker(this);
       Material material=generateMaterial(createAssignmentRequest.getMaterialDto(),materialMaker,materialFiles,cid,request);
       return assignmentService.createAssignment(material,assignmentMapper.toEntity(createAssignmentRequest));
    }

    public  Material generateMaterial(Material material, List<MultipartFile> materialFiles, UUID cid, HttpServletRequest request) throws IOException {
        ClassRoom classroom=fetchClassRoom(request,cid);
        classroom.addMaterial(material);
        if(materialFiles!=null&&!materialFiles.isEmpty()){
            uploadFiles(materialFiles,classroom,material);
        }
        return materialRepository.save(material);
    }

    @Override
    @Transactional
    public AssignmentResponseDto editAssignment(UUID mid, CreateAssignmentRequest createAssignmentRequest, List<MultipartFile> files, List<String> toDeleteFiles, HttpServletRequest request) throws IOException {
        Material material=fetchMaterial(mid,request);
        if (!material.getMaterialType().equals(MaterialType.ASSIGNMENT)&&material.getAssignment()==null) {
            throw new IllegalArgumentException("Material is not an assignment");
        }
        editMaterial(material,createAssignmentRequest.getMaterialDto(),files,toDeleteFiles);
        return assignmentService.editAssignment(material,createAssignmentRequest);
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
    public MaterialResponseDto editMaterial(UUID mid, MaterialDto materialDto, List<MultipartFile> files, List<String> toDeleteFiles,HttpServletRequest request) throws IOException {
      return editMaterial(fetchMaterial(mid,request),materialDto,files,toDeleteFiles);
    }

    private MaterialResponseDto editMaterial(Material material,MaterialDto materialDto, List<MultipartFile> files, List<String> ToDeleteFiles) throws IOException {
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
        material.setDescription(materialDto.getDescription());
        material.setHeadLine(materialDto.getHeadLine());
    }

    private Material generateMaterial(MaterialDto materialDto,MaterialMaker materialMaker,List<MultipartFile> materialFiles,UUID cid,HttpServletRequest request) throws IOException {
        Material material=materialMapper.toEntity(materialDto);
        materialMaker.setMaterialType(material);
        return materialMaker.generateMaterial(material,materialFiles,cid,request);
    }
}
