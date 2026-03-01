package com.unihub.auth.internal.controller;

import com.unihub.auth.internal.dto.request.SystemAdminRequest;
import com.unihub.auth.internal.dto.response.SystemAdminResponse;
import com.unihub.auth.internal.service.IInternalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal/user")
public class InternalController {

    private final IInternalService internalService;

    @PostMapping("/create")
    public ResponseEntity<SystemAdminResponse> createSystemAdmin(@Valid @RequestBody SystemAdminRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(internalService.createSystemAdmin(request));
    }
}
