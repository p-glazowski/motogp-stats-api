package com.pglazowski.motogpstatsapi.exceptions;

import com.pglazowski.motogpstatsapi.dto.ResponseError;
import com.pglazowski.motogpstatsapi.dto.ValidationResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseError> handleNotFoundException(NotFoundException ex) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        return ResponseEntity.status(httpStatus)
                .body(new ResponseError(
                        httpStatus.value(),
                        httpStatus.getReasonPhrase(),
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationResponseError> handleValidationException(
            MethodArgumentNotValidException ex) {

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        Map<String, String> validationErrors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(error.getField(), error.getDefaultMessage());
        }

        ValidationResponseError response = new ValidationResponseError(
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                "Validation failed",
                LocalDateTime.now(),
                validationErrors
        );

        return ResponseEntity.status(httpStatus).body(response);
    }
}
