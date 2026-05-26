package com.unihub.auth.internal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.unihub.auth.internal.dto.request.SystemAdminRequest;
import com.unihub.auth.internal.dto.response.DashBoardAggregatesDto;
import com.unihub.auth.internal.dto.response.SystemAdminResponse;
import com.unihub.auth.internal.service.IInternalService;
import com.unihub.auth.security.dto.response.UserDto;
import com.unihub.auth.security.service.impl.ProjectUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal/user")
public class InternalController {

    private final IInternalService internalService;

    private final ProjectUserDetailsService userDetailsService;

    @PostMapping("/create")
    public ResponseEntity<SystemAdminResponse> createSystemAdmin(@Valid @RequestBody SystemAdminRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(internalService.createSystemAdmin(request));
    }

    @GetMapping("/get-users-count/{cid}")
    public ResponseEntity<Long> countUsers(@PathVariable UUID cid){
        return ResponseEntity.ok(internalService.countUsers(cid));
    }

    @GetMapping("/analysis")
    public ResponseEntity<DashBoardAggregatesDto> getUserAnalysis(@RequestParam UUID tid) throws JsonProcessingException {
        return ResponseEntity.ok(internalService.analysis(tid));
    }

    @GetMapping("/user-info")
    public ResponseEntity<UserDto> getUserInfo(@RequestHeader("X-User-Email") String email){
        return ResponseEntity.ok(userDetailsService.getUserInfo(email));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam("q") String keyword,
                                                     @RequestParam("tid") UUID tid,
                                                     @RequestParam(value = "cid", required = false) UUID cid){
        return ResponseEntity.ok(internalService.searchUsers(keyword, tid, cid));
    }
}
