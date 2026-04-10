package com.unihub.classroom.clazz.controller;

import com.unihub.classroom.clazz.dto.ClassRoomResponse;
import com.unihub.classroom.clazz.dto.CreateClassroomDto;
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
}
