package com.marko.BlogPlatform.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException e) {
        logger.error("Exception occurred", e);
        ApiError error = new ApiError(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Resource not found", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(CustomAccessDeniedException.class)
    public ResponseEntity<ApiError> handleCustomAccessDeniedException(CustomAccessDeniedException e) {
        logger.error("Exception occurred", e);
        ApiError error = new ApiError(LocalDateTime.now(), HttpStatus.FORBIDDEN.value(), "Access denied!", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiError> handleAuthorizationDenied(AuthorizationDeniedException e) {
        logger.error("Exception occurred", e);
        ApiError error = new ApiError(LocalDateTime.now(), HttpStatus.FORBIDDEN.value(), "Access denied!", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException e) {
        logger.error("Exception occurred", e);
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ApiError error = new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                "Validation failed", message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception e) {
        logger.error("Exception occurred", e);
        ApiError error = new ApiError(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unexpected error", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


