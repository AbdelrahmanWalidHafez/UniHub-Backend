package com.unihub.classroom.internal.controller;

import com.unihub.classroom.clazz.dto.response.ClassRoomsDto;
import com.unihub.classroom.clazz.service.IClassRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal")
public class InternalController {

    private final IClassRoomService classRoomService;

    @GetMapping("/get-classes")
    public ResponseEntity<ClassRoomsDto> getClasses(@RequestHeader("X-User-Email") String email){
        return ResponseEntity.ok(ClassRoomsDto.builder().classRooms(classRoomService.fetchAllClassRoomsForUser(email)).build());
    }
}
