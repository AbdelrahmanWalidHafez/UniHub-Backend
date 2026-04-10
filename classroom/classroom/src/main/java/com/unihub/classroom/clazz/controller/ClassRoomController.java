package com.unihub.classroom.clazz.controller;

import com.unihub.classroom.clazz.dto.ClassRoomResponse;
import com.unihub.classroom.clazz.dto.CreateClassroomDto;
import com.unihub.classroom.clazz.dto.MemberDto;
import com.unihub.classroom.clazz.dto.MembersDto;
import com.unihub.classroom.clazz.service.IClassRoomService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.Path;
import lombok.Getter;
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

}
