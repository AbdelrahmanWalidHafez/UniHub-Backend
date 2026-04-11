package com.unihub.classroom.clazz.service;

import com.unihub.classroom.clazz.dto.OwnerDto;
import com.unihub.classroom.clazz.dto.ClassRoomResponse;
import com.unihub.classroom.clazz.dto.CreateClassroomDto;
import com.unihub.classroom.clazz.dto.MemberDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface IClassRoomService {

    ClassRoomResponse createClassRoom(CreateClassroomDto createClassroomDto, HttpServletRequest request);

    ClassRoomResponse archiveClassRoom(UUID id,HttpServletRequest request);

    MemberDto joinClassRoom(String code, HttpServletRequest request);

    void leaveClassRoom(UUID id, HttpServletRequest request);

    List<MemberDto> fetchMembers(UUID id, HttpServletRequest request, int pageNum);

    OwnerDto fetchOwner(UUID id, HttpServletRequest request);

    List<ClassRoomResponse> fetchEnrolledClassRooms(HttpServletRequest request);


    List<ClassRoomResponse> fetchArchivedClassRooms(HttpServletRequest request);

    List<ClassRoomResponse> fetchMyClassRooms(HttpServletRequest request);

}
