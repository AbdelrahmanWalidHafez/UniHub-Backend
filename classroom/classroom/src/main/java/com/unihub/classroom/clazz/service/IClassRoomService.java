package com.unihub.classroom.clazz.service;

import com.unihub.classroom.clazz.dto.ClassRoomResponse;
import com.unihub.classroom.clazz.dto.CreateClassroomDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface IClassRoomService {

    ClassRoomResponse createClassRoom(CreateClassroomDto createClassroomDto, HttpServletRequest request);

    ClassRoomResponse archiveClassRoom(UUID id,HttpServletRequest request);
}
