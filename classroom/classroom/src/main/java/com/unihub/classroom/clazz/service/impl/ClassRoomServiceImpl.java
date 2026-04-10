package com.unihub.classroom.clazz.service.impl;

import com.unihub.classroom.clazz.dto.ClassRoomResponse;
import com.unihub.classroom.clazz.dto.CreateClassroomDto;
import com.unihub.classroom.clazz.mapper.ClassRoomMapper;
import com.unihub.classroom.clazz.model.ClassRoom;
import com.unihub.classroom.clazz.repository.ClassRoomRepository;
import com.unihub.classroom.clazz.service.IClassRoomService;
import com.unihub.classroom.clazz.service.state.ClassRoomContext;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassRoomServiceImpl  implements IClassRoomService {

    private final ClassRoomMapper classRoomMapper;

    private final ClassRoomRepository classRoomRepository;

    @Override
    public ClassRoomResponse createClassRoom(CreateClassroomDto createClassroomDto, HttpServletRequest request) {
        ClassRoom classRoom = classRoomMapper.toEntity(createClassroomDto);
        classRoom.setImageNum(getImageNum());
        classRoom.setCode(generateCode());
        classRoom.setArchived(false);
        classRoom.setUniversityId(fetchUidFromHeader(request));
        classRoom.setCollegeId(fetchCidFromHeader(request));
        classRoomRepository.save(classRoom);
        return classRoomMapper.toDto(classRoom);
    }

    @Override
    public ClassRoomResponse archiveClassRoom(UUID id,HttpServletRequest request) {
        ClassRoom classRoom = fetchClassRoom(id,request);
        ClassRoomContext classRoomContext =initContext(classRoom);
        classRoomContext.handleRequest(classRoom);
        classRoomRepository.save(classRoom);
        return classRoomMapper.toDto(classRoom);
    }


    private int getImageNum(){
        return new Random().nextInt(10)+1;
    }

    private String generateCode() {
        return UUID.randomUUID().toString().replace("-","").substring(0, 6);
    }

    private ClassRoomContext initContext(ClassRoom classRoom){
        ClassRoomContext classRoomContext = new ClassRoomContext();
        classRoomContext.setState(classRoom);
        return classRoomContext;
    }

    private UUID fetchUidFromHeader(HttpServletRequest request){
        return UUID.fromString(request.getHeader("X-User-University-Id"));
    }

    private UUID fetchCidFromHeader(HttpServletRequest request){
        return UUID.fromString(request.getHeader("X-User-College-Id"));
    }

    public String fetchEmailFromHeader(HttpServletRequest request){
        return request.getHeader("X-User-Email");
    }

    private ClassRoom fetchClassRoom(UUID id,HttpServletRequest request){
        return classRoomRepository.findByIdAndCollegeIdAndUniversityIdAndCreatedBy(id
                        ,fetchCidFromHeader(request)
                        ,fetchUidFromHeader(request)
                        ,fetchEmailFromHeader(request)).orElseThrow(()->new EntityNotFoundException("Classroom not found with id: "+id));
    }

}
