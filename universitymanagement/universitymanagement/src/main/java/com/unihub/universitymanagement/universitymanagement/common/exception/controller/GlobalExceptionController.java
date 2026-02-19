package com.unihub.universitymanagement.universitymanagement.common.exception.controller;

import com.unihub.universitymanagement.universitymanagement.common.exception.dto.ErrorResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
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
public class GlobalExceptionController {

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