package com.unihub.classroom.material.service.impl;

import com.unihub.classroom.assginement.dto.request.CreateAssignmentRequest;
import com.unihub.classroom.assginement.dto.response.AssignmentResponseDto;
import com.unihub.classroom.assginement.mapper.AssignmentMapper;
import com.unihub.classroom.assginement.service.IAssignmentService;
import com.unihub.classroom.clazz.model.ClassRoom;
import com.unihub.classroom.clazz.repository.ClassRoomRepository;
import com.unihub.classroom.material.dto.request.MaterialDto;
import com.unihub.classroom.material.dto.response.MaterialResponseDto;
import com.unihub.classroom.material.mapper.MaterialMapper;
import com.unihub.classroom.material.model.Material;
import com.unihub.classroom.material.model.enums.MaterialType;
import com.unihub.classroom.material.repository.MaterialRepository;
import com.unihub.classroom.material.service.IMaterialService;
import com.unihub.classroom.material.template.MaterialMaker;
import com.unihub.classroom.material.template.concrete.AnnouncementMaker;
import com.unihub.classroom.material.template.concrete.AssignmentMaker;
import com.unihub.classroom.utils.FileUtils;
import com.unihub.classroom.utils.HttpHeadersUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final FileUtils fileUtils;

    private final MaterialMapper materialMapper;

    private final AssignmentMapper assignmentMapper;

    private final HttpHeadersUtils httpHeadersUtils;

    private final IAssignmentService assignmentService;

    private final MaterialRepository materialRepository;

    private final ClassRoomRepository classRoomRepository;

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
            //TODO UPLOAD THE FILES IN VECTOR DB AT AI MS
            fileUtils.uploadFiles(materialFiles,classroom, material, Material::getMaterialUrls);
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
            material.getMaterialUrls().forEach(fileUtils::deleteFile);
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
            fileUtils.uploadFiles(files,material.getClassroom(), material, Material::getMaterialUrls);
        }
        if(ToDeleteFiles!=null&&!ToDeleteFiles.isEmpty()){
            ToDeleteFiles.forEach(fileUtils::deleteFile);
            material.getMaterialUrls().removeAll(ToDeleteFiles);
        }
        return materialMapper.toDto(materialRepository.save(material));
    }

    @Override
    @Transactional
    public MaterialResponseDto getMaterial(UUID mid, HttpServletRequest request) {
        return materialMapper.toDto(
                materialRepository
                        .findMaterial(mid, httpHeadersUtils.fetchEmailFromHeader(request))
                        .orElseThrow(()->new EntityNotFoundException("Material not found with id: "+mid)
                        )
        );
    }

    @Override
    @Transactional
    public List<MaterialResponseDto> getAllMaterials(UUID id, HttpServletRequest request, int pageNum){
        return materialRepository
                .findMaterials(httpHeadersUtils.fetchEmailFromHeader(request), id,generatePageable(pageNum))
                .stream()
                .map(materialMapper::toDto).toList();
    }


    @Override
    @Transactional
    public List<MaterialResponseDto> getAllAssignments(UUID id, HttpServletRequest request, int pageNum){
        return materialRepository
                .findAssignments(httpHeadersUtils.fetchEmailFromHeader(request), id,MaterialType.ASSIGNMENT,generatePageable(pageNum))
                .stream()
                .map(materialMapper::toDto).toList();
    }

    private ClassRoom fetchClassRoom(HttpServletRequest request,UUID id){
        return classRoomRepository
                .findByCreatedByAndId(httpHeadersUtils.fetchEmailFromHeader(request),id )
                .orElseThrow(()->new EntityNotFoundException("Classroom not found with id: "+id));
    }

    private Material fetchMaterial(UUID mid,HttpServletRequest request){
        return materialRepository.findByMidAndCreatedBy(mid, httpHeadersUtils.fetchEmailFromHeader(request))
                .orElseThrow(()->new EntityNotFoundException("Material not found with id: "+mid));
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

    private Pageable generatePageable(int pageNum){
        return PageRequest.of(pageNum-1,5, Sort.by("createdAt").descending());
    }
}
