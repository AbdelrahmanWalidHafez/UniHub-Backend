package com.unihub.s3.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@AllArgsConstructor
public class ErrorResponseDto {

    private LocalDateTime timeStamp;

    private HttpStatusCode httpStatusCode;

    private String error;

    private String message;

    private String path;

    private List<String> errors;
}