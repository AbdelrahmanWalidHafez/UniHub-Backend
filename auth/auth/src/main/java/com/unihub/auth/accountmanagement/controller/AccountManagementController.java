package com.unihub.auth.accountmanagement.controller;

import com.unihub.auth.accountmanagement.dto.request.BaseUserRequest;
import com.unihub.auth.accountmanagement.dto.request.UpdateUserRequest;
import com.unihub.auth.accountmanagement.dto.response.UserMetaDataResponses;
import com.unihub.auth.accountmanagement.job.dto.JobResultResponse;
import com.unihub.auth.accountmanagement.service.IAccountManagementService;
import com.unihub.auth.accountmanagement.strategy.UserRoles;
import com.unihub.auth.security.dto.response.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account-management")
public class AccountManagementController {

    private  final IAccountManagementService service;

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody BaseUserRequest userRequest, Authentication authentication){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(userRequest,authentication));
    }

    @PostMapping("/import")
    public ResponseEntity<JobResultResponse> importFromCsvToDbJob(@RequestPart(name = "file")MultipartFile file, Authentication authentication) throws Exception{
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insertFromCsv(file,authentication));
    }

    @GetMapping("/get-user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID id,Authentication authentication){
        return ResponseEntity.ok(service.getUser(id,authentication));
    }

    @GetMapping("/get-users")
    public ResponseEntity<UserMetaDataResponses> getUsers(@RequestParam(name = "page_num", defaultValue = "1") int pageNum,
                                                          @RequestParam(value = "sort_dir", defaultValue = "desc") String sortDir,
                                                          @RequestParam(value = "sort_field", defaultValue = "createdAt") String sortField,
                                                          @RequestParam(value="role_name",required = false) UserRoles roleName,
                                                          @RequestParam(value = "cid",required = false)UUID cid,
                                                          Authentication authentication){
        return ResponseEntity.ok(
              UserMetaDataResponses.builder()
                      .usersList(
                              service.getUsers(pageNum,sortDir,sortField,roleName,cid,authentication)
                      )
                      .build()
        );
    }

    @GetMapping("/search-user")
    public ResponseEntity<UserMetaDataResponses> searchUser(@RequestParam("search_text")String searchText,Authentication authentication){
        return ResponseEntity.ok(UserMetaDataResponses.builder().usersList(service.searchUser(searchText,authentication)).build());
    }

    @PatchMapping("/update-user-gpa/{id}")
    public ResponseEntity<UserDto> updateGpa(@PathVariable UUID id,@Valid @RequestBody Double gpa,Authentication authentication){
        return ResponseEntity.ok(service.updateGpa(id,gpa,authentication));
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id,  @Valid @RequestBody UpdateUserRequest userRequest, Authentication authentication){
        return  ResponseEntity.ok(service.updateUser(id,userRequest,authentication));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id,Authentication authentication){
        service.deleteUser(id,authentication);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping ("/delete-batch")
    public ResponseEntity<?> deleteUsers(@Valid @RequestBody List<UUID> ids){
        service.deleteUsers(ids);
        return ResponseEntity.noContent().build();
    }

}
