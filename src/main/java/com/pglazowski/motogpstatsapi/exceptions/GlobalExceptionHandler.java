package com.pglazowski.motogpstatsapi.exceptions;

import com.pglazowski.motogpstatsapi.dto.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

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
}
