package com.jcv.hyperclean.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

import static com.jcv.hyperclean.util.ListUtils.listToMap;

@RestControllerAdvice
public class BaseControllerAdvice {
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = listToMap(ex.getBindingResult().getAllErrors(), error -> ((FieldError) error).getField(), ObjectError::getDefaultMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        String message = ex.getMessage();
        String entityName = extractEntityName(message);

        String responseMessage = entityName != null
                ? entityName + " not found."
                : "Resource not found.";

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
    }

    private String extractEntityName(String message) {
        if (message == null) {
            return null;
        }

        String[] parts = message.split(" ");
        if (parts.length > 3) {
            String fullClassName = parts[3]; // com.example.Customer
            return fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        }
        return null;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
