package com.biwaby.financialtracker.exception;

import com.biwaby.financialtracker.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<ErrorResponse> handleException(ResponseException e) {
        return ResponseEntity.status(e.getStatusCode()).body(
                new ErrorResponse(
                        e.getMessage(),
                        HttpStatusCode.valueOf(e.getStatusCode()).toString(),
                        e.getClass().getName()
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(
                        e.getBindingResult().getAllErrors().getFirst().getDefaultMessage(),
                        HttpStatus.BAD_REQUEST.toString(),
                        e.getClass().getName()
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponse(
                        e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                        e.getClass().getName()
                )
        );
    }
}
