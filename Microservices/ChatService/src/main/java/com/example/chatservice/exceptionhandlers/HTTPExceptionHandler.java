package com.example.chatservice.exceptionhandlers;

import com.example.chatservice.utils.ExceptionLoggingUtil;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;

@Slf4j
@Order(1)
@ControllerAdvice
public class HTTPExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormatException) {
            if (invalidFormatException.getTargetType().isEnum()) {
                String errorMessage = "Invalid value for enum type: " + invalidFormatException.getValue() +
                        ". Accepted values are: " + Arrays.toString(invalidFormatException.getTargetType().getEnumConstants());
                ExceptionLoggingUtil.logStructuredError("InvalidFormatException", errorMessage, request, ex);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }
        }
        ExceptionLoggingUtil.logStructuredError("HttpMessageNotReadableException", ex.getMessage(), request, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Malformed JSON request: " + ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        if (ex.getRequiredType() == null) {
            throw new IllegalArgumentException("Required type for parameter '" + ex.getName() + "' is null.");
        }
        String errorMessage = "Invalid value for parameter '" + ex.getName() + "': " + ex.getValue() +
                ". Expected type: " + ex.getRequiredType().getSimpleName();
        ExceptionLoggingUtil.logStructuredError("MethodArgumentTypeMismatchException", errorMessage, request, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        String errorMessage = "HTTP method " + ex.getMethod() + " is not supported for this endpoint. Supported methods are: " + ex.getSupportedHttpMethods();
        ExceptionLoggingUtil.logStructuredError("HttpRequestMethodNotSupportedException", errorMessage, request, ex);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorMessage);
    }
}
