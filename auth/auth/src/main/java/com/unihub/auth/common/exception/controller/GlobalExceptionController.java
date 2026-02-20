package com.unihub.auth.common.exception.controller;

import com.unihub.auth.common.exception.dto.ErrorResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionController  {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthentication(
            AuthenticationException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponseDto.builder()
                        .timeStamp(LocalDateTime.now())
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .httpStatusCode(HttpStatus.UNAUTHORIZED)
                        .errors(List.of(ex.getMessage()))
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponseDto.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Access is denied")
                        .path(request.getRequestURI())
                        .httpStatusCode(HttpStatus.FORBIDDEN)
                        .errors(List.of(ex.getMessage()))
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (msg1, msg2) -> msg1
                ));

        return ResponseEntity.badRequest().body(errors);
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(Exception ex, HttpServletRequest request) {
        ErrorResponseDto errorResponseDTO = ErrorResponseDto.builder()
                .timeStamp(LocalDateTime.now())
                .httpStatusCode(HttpStatus.BAD_REQUEST)
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .path(request.getRequestURI())
                .message(ex.getMessage())
                .errors(List.of(ex.getLocalizedMessage()))
                .build();
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = "Invalid request data";

        if (isDuplicateKey(ex)) {
            status = HttpStatus.CONFLICT;
            message = "resource already exists";
        }

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .timeStamp(LocalDateTime.now())
                .httpStatusCode(status)
                .error(status.getReasonPhrase())
                .path(request.getRequestURI())
                .message(message)
                .build();

        return ResponseEntity.status(status).body(errorResponse);
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

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodNotAllowedException(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDto errorResponseDTO = ErrorResponseDto.builder()
                .timeStamp(LocalDateTime.now())
                .httpStatusCode(HttpStatusCode.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()))
                .error(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
                .path(request.getRequestURI())
                .message("Request method '" + ex.getMethod() + "' not allowed for this endpoint")
                .errors(List.of(ex.getLocalizedMessage()))
                .build();

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.METHOD_NOT_ALLOWED);
    }


    private boolean isDuplicateKey(DataIntegrityViolationException ex) {
        Throwable cause = ex.getCause();
        while (cause != null) {
            if (cause instanceof org.hibernate.exception.ConstraintViolationException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }
}

