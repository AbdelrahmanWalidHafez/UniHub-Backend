package com.unihub.auth.accountmanagement.service;

import com.unihub.auth.accountmanagement.dto.request.BaseUserRequest;
import com.unihub.auth.accountmanagement.dto.request.UpdateUserRequest;
import com.unihub.auth.accountmanagement.dto.response.UserMetaDataResponse;
import com.unihub.auth.accountmanagement.job.dto.JobResultResponse;
import com.unihub.auth.accountmanagement.strategy.UserRoles;
import com.unihub.auth.security.dto.response.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IAccountManagementService {

    UserDto createUser(BaseUserRequest userRequest, Authentication authentication);

    JobResultResponse insertFromCsv(MultipartFile file, Authentication authentication) throws Exception;

    UserDto getUser(UUID id, Authentication authentication);

    List<UserMetaDataResponse>getUsers(int pageNum,
                                       String sortDir,
                                       String sortField,
                                       UserRoles roleName,
                                       UUID uuid ,
                                       Authentication authentication);
    List<UserMetaDataResponse> searchUser(String searchText);

    UserDto updateUser(UUID id, UpdateUserRequest userRequest, Authentication authentication);

    UserDto updateGpa(UUID id,Double gpa,Authentication authentication);

    void deleteUser(UUID id, Authentication authentication);

    void deleteUsers(List<UUID>ids);

}
