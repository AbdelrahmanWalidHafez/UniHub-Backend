package com.unihub.auth.internal.service;

import com.unihub.auth.internal.dto.request.SystemAdminRequest;
import com.unihub.auth.internal.dto.response.SystemAdminResponse;

public interface IInternalService {

    SystemAdminResponse createSystemAdmin(SystemAdminRequest request);

}
