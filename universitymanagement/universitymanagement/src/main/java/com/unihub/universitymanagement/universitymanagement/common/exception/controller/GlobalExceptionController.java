package com.unihub.universitymanagement.universitymanagement.common.exception.controller;

import com.unihub.universitymanagement.universitymanagement.common.exception.dto.ErrorResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionController {

    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();
        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        String message = "Data integrity violation";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (ex.getCause() != null && ex.getCause().getMessage().contains("Duplicate entry")) {
            message = "resource already exists";
            status = HttpStatus.CONFLICT;
        }
        ErrorResponseDto ErrorResponseDTO = ErrorResponseDto.builder()
                .timeStamp(LocalDateTime.now())
                .httpStatusCode(status)
                .error(status.getReasonPhrase())
                .path(request.getRequestURI())
                .message(message)
                .errors(List.of(ex.getLocalizedMessage()))
                .build();
        return new ResponseEntity<>(ErrorResponseDTO, status);
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

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErrorResponseDto> handlePropertyReferenceException(IOException ex, HttpServletRequest request) {
        ErrorResponseDto errorResponseDTO = ErrorResponseDto.builder()
                .timeStamp(LocalDateTime.now())
                .httpStatusCode(HttpStatus.BAD_REQUEST)
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .path(request.getRequestURI())
                .message(ex.getMessage())
                .errors(List.of(ex.getLocalizedMessage()))
                .build();
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}