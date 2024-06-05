package com.apt.project.shared.exception;

import com.apt.project.auth.security.jwt.AuthEntryPointJwt;
import com.apt.project.shared.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(Exception ex) {
        logger.error(ex.toString());
        if(ex instanceof MethodArgumentNotValidException e) {
            BindingResult bindingResult =  e.getBindingResult();
            FieldError fieldError = bindingResult.getFieldError();
            String errorMessage = "Validation failed for field '" + (fieldError != null ? fieldError.getField() : null) + "': " + (fieldError != null ? fieldError.getDefaultMessage() : null);
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        }

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
