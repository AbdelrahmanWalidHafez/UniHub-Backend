package com.unihub.usage.controller;

import com.unihub.usage.dto.response.DashboardResponseDTO;
import com.unihub.usage.service.IDashBoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/usage")
public class AnalysisController {

    private final IDashBoardService service;

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getDashBoardAnalysis(HttpServletRequest request){
        return ResponseEntity.ok(service.getDashBoard(request));
    }
}
