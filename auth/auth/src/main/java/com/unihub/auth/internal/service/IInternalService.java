package com.unihub.auth.internal.service;

import com.unihub.auth.internal.dto.request.SystemAdminRequest;
import com.unihub.auth.internal.dto.response.DashBoardAggregatesDto;
import com.unihub.auth.internal.dto.response.SystemAdminResponse;
import com.unihub.auth.security.dto.response.UserDto;

import java.util.List;
import java.util.UUID;

public interface IInternalService {

    SystemAdminResponse createSystemAdmin(SystemAdminRequest request);

    Long countUsers(UUID cid);

    DashBoardAggregatesDto analysis(UUID tid);

    List<UserDto> searchUsers(String keyword, UUID tid, UUID cid);
}
