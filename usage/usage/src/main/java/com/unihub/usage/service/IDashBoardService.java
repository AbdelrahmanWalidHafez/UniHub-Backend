package com.unihub.usage.service;

import com.unihub.usage.dto.response.DashboardResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface IDashBoardService {

    DashboardResponseDTO getDashBoard(HttpServletRequest servletRequest);
}
