package com.unihub.s3.common.exception;

import com.unihub.s3.common.dto.ErrorResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionController  {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleExceptionInternal(Exception ex, HttpServletRequest request) {
        ErrorResponseDto errorResponseDTO = ErrorResponseDto.builder()
                .timeStamp(LocalDateTime.now())
                .httpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .path(request.getRequestURI())
                .message(ex.getMessage())
                .errors(List.of(ex.getLocalizedMessage()))
                .build();
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        ErrorResponseDto errorResponseDTO = ErrorResponseDto.builder()
                .timeStamp(LocalDateTime.now())
                .httpStatusCode(HttpStatus.BAD_REQUEST)
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .path(request.getRequestURI())
                .message(ex.getMessage())
                .errors(List.of(ex.getLocalizedMessage()))
                .build();
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request) {
        ErrorResponseDto errorResponseDTO = ErrorResponseDto.builder()
                .timeStamp(LocalDateTime.now())
                .httpStatusCode(HttpStatus.NOT_FOUND)
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .path(request.getRequestURI())
                .message(ex.getMessage())
                .errors(List.of(ex.getLocalizedMessage()))
                .build();
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponseDto> handleIOException(IOException ex, HttpServletRequest request) {
        ErrorResponseDto errorResponseDTO = ErrorResponseDto.builder()
                .timeStamp(LocalDateTime.now())
                .httpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .path(request.getRequestURI())
                .message(ex.getMessage())
                .errors(List.of(ex.getLocalizedMessage()))
                .build();
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}