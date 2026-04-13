package com.unihub.classroom.clazz.controller;

import com.unihub.classroom.clazz.dto.request.CreateClassroomDto;
import com.unihub.classroom.clazz.dto.response.*;
import com.unihub.classroom.clazz.service.IClassRoomService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/classroom")
public class ClassRoomController {

    private final IClassRoomService classRoomService;

    @PostMapping("/instructor/create")
    public ResponseEntity<ClassRoomResponse> createClassRoom(@Valid @RequestBody CreateClassroomDto createClassroomDto, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(classRoomService.createClassRoom(createClassroomDto,request));
    }

    @PatchMapping("/instructor/archive/{id}")
    public ResponseEntity<ClassRoomResponse> archiveClassRoom(@PathVariable UUID id,HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(classRoomService.archiveClassRoom(id,request));
    }

    @PostMapping("/join")
    public ResponseEntity<MemberDto> joinClassRoom(@RequestParam String code, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(classRoomService.joinClassRoom(code,request));
    }

    @DeleteMapping("/leave/{id}")
    public ResponseEntity<Void> leaveClassRoom(@PathVariable UUID id, HttpServletRequest request){
        classRoomService.leaveClassRoom(id,request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-members/{id}")
    public ResponseEntity<MembersDto>fetchMembers(@PathVariable UUID id, @RequestParam(value="page_num",defaultValue ="1") int pageNum, HttpServletRequest request){
        return ResponseEntity.ok(MembersDto.builder().members(classRoomService.fetchMembers(id,request,pageNum)).build());
    }

    @GetMapping("/get-owner/{id}")
    public ResponseEntity<OwnerDto>fetchOwner(@PathVariable UUID id, HttpServletRequest request){
        return ResponseEntity.ok(classRoomService.fetchOwner(id,request));
    }

    @GetMapping("/get-enrolled-classes")
    public ResponseEntity<ClassRoomsDto> fetchEnrolledClasses(HttpServletRequest request){
        return  ResponseEntity.ok(ClassRoomsDto.builder().classRooms(classRoomService.fetchEnrolledClassRooms(request)).build());
    }

    @GetMapping("/fetch-archived-classes")
    public ResponseEntity<ClassRoomsDto> getArchivedClasses(HttpServletRequest request){
        return  ResponseEntity.ok(ClassRoomsDto.builder().classRooms(classRoomService.getArchivedClassRooms(request)).build());
    }

    @GetMapping("/instructor/get-archived-classes")
    public ResponseEntity<ClassRoomsDto> fetchArchivedClasses(HttpServletRequest request){
        return  ResponseEntity.ok(ClassRoomsDto.builder().classRooms(classRoomService.fetchArchivedClassRooms(request)).build());
    }

    @GetMapping("/instructor/get-my-classes")
    public ResponseEntity<ClassRoomsDto> fetchMyClasses(HttpServletRequest request){
        return  ResponseEntity.ok(ClassRoomsDto.builder().classRooms(classRoomService.fetchMyClassRooms(request)).build());
    }

    @GetMapping("/instructor/get-active-classes")
    public ResponseEntity<ClassRoomsDto> fetchActiveClasses(HttpServletRequest request){
        return ResponseEntity.ok(ClassRoomsDto.builder().classRooms(classRoomService.fetchActiveClassRooms(classRoomService.fetchEmailFromHeader(request),request)).build());
    }

}
