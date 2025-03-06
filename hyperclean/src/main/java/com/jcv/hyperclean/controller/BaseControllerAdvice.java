package com.jcv.hyperclean.controller;

import com.jcv.hyperclean.dto.AppointmentDTO;
import com.jcv.hyperclean.dto.ResponseDTO;
import com.jcv.hyperclean.exception.HCInvalidDateTimeFormat;
import com.jcv.hyperclean.exception.HCNotFoundException;
import com.jcv.hyperclean.exception.HCValidationFailedException;
import com.jcv.hyperclean.exception.HCVehicleTimeSlotOccupiedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jcv.hyperclean.util.ListUtils.mapList;

@RestControllerAdvice
public class BaseControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<List<Map<String, String>>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errorList = mapList( ex.getBindingResult().getFieldErrors(), fieldError -> Map.of(
                "field", fieldError.getField(),
                "error", fieldError.getDefaultMessage()
        ));

        return ResponseEntity.badRequest().body(ResponseDTO.of(errorList, "Validation failed. Please check the provided data."));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDTO<Map<String, String>>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Data Integrity Violation");
        errorResponse.put("message", ex.getRootCause() != null ? ex.getRootCause().getMessage() : "Unknown error");

        return ResponseEntity.badRequest().body(ResponseDTO.of(errorResponse, "There's already an instance with that data."));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HCValidationFailedException.class)
    public ResponseEntity<ResponseDTO<Object>> handleHCValidationFailedException(HCValidationFailedException ex) {
        return ResponseEntity.badRequest().body(ResponseDTO.of(ex.getEntity(), ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HCVehicleTimeSlotOccupiedException.class)
    public ResponseEntity<ResponseDTO<AppointmentDTO>> handleHCVehicleTimeSlotOccupiedException(HCVehicleTimeSlotOccupiedException ex) {
        return ResponseEntity.badRequest().body(ResponseDTO.of(ex.getConflictingAppointment(), ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDTO<String>> handleMessageNotReadableException() {
        return ResponseEntity.badRequest().body(ResponseDTO.of(null, "The request body is invalid. Please review it and try again."));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HCInvalidDateTimeFormat.class)
    public ResponseEntity<ResponseDTO<String>> handleHCInvalidDateTimeFormat(HCInvalidDateTimeFormat ex) {
        return ResponseEntity.badRequest().body(ResponseDTO.of(ex.getInvalidDate(), ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HCNotFoundException.class)
    public ResponseEntity<ResponseDTO<Object>> handleHCNotFoundException(HCNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO.of(null, ex.getMessage()));
    }
}


