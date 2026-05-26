package com.unihub.chat.common.exception.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ErrorResponseDto {

    @JsonProperty("time_stamp")
    private LocalDateTime timeStamp;

    @JsonProperty("http_status_code")
    private HttpStatusCode httpStatusCode;

    private String error;

    private String message;

    private String path;

    @JsonProperty("errors")
    private List<String> errors;
}