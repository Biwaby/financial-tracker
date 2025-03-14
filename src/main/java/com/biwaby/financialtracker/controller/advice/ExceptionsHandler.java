package com.biwaby.financialtracker.controller.advice;

import com.biwaby.financialtracker.dto.response.ErrorResponse;
import com.biwaby.financialtracker.exception.ResponseException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@Component
@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<ErrorResponse> handleException(ResponseException e) {
        return ResponseEntity.status(e.getStatusCode()).body(
                new ErrorResponse(
                        e.getMessage(),
                        HttpStatusCode.valueOf(e.getStatusCode()).toString(),
                        e.getExceptionClassName()
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String errorMessages = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" "));
        return ResponseEntity.status(e.getStatusCode()).body(
                new ErrorResponse(
                        errorMessages,
                        e.getStatusCode().toString(),
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
