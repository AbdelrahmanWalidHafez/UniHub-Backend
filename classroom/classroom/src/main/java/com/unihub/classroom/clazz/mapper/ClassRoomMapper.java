package com.unihub.classroom.clazz.mapper;

import com.unihub.classroom.clazz.dto.ClassRoomResponse;
import com.unihub.classroom.clazz.dto.CreateClassroomDto;
import com.unihub.classroom.clazz.model.ClassRoom;
import org.springframework.stereotype.Component;

@Component
public class ClassRoomMapper {

    public ClassRoom toEntity(CreateClassroomDto createClassroomDto) {
        ClassRoom classRoom = new ClassRoom();
        classRoom.setClassTitle(createClassroomDto.getClassTitle());
        classRoom.setClassSubTitle(createClassroomDto.getClassSubTitle());
        return classRoom;
    }

    public ClassRoomResponse toDto(ClassRoom classRoom) {
        ClassRoomResponse classRoomResponse = new ClassRoomResponse();
        classRoomResponse.setId(classRoom.getId());
        classRoomResponse.setCode(classRoom.getCode());
        classRoomResponse.setClassTitle(classRoom.getClassTitle());
        classRoomResponse.setClassSubTitle(classRoom.getClassSubTitle());
        classRoomResponse.setImageNum(classRoom.getImageNum());
        classRoomResponse.setCreatedAt(classRoom.getCreatedAt());
        classRoomResponse.setUpdatedAt(classRoom.getUpdatedAt());
        classRoomResponse.setCreatedBy(classRoom.getCreatedBy());
        classRoomResponse.setUpdatedBy(classRoom.getUpdatedBy());
        classRoomResponse.setArchived(classRoom.isArchived());
        classRoomResponse.setUniversityId(classRoom.getUniversityId());
        classRoomResponse.setCollegeId(classRoom.getCollegeId());
        return classRoomResponse;
    }
}
