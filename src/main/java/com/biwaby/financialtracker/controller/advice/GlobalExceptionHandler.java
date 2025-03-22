package com.biwaby.financialtracker.controller.advice;

import com.biwaby.financialtracker.dto.response.ErrorResponse;
import com.biwaby.financialtracker.exception.ResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@Component
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private Throwable getRootCauseMessage(Throwable throwable) {
        Throwable cause = throwable.getCause();
        if (cause != null) {
            return getRootCauseMessage(cause);
        }
        return throwable;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        Throwable cause = getRootCauseMessage(e);
        logger.error("Unhandled exception occurred: {}", cause.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponse(
                        cause.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                        cause.getClass().getName()
                )
        );
    }

    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<ErrorResponse> handleException(ResponseException e) {
        logger.error("Response error: {}", e.getMessage(), e);
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
        logger.error("Validation errors: {}", errorMessages, e);
        return ResponseEntity.status(e.getStatusCode()).body(
                new ErrorResponse(
                        errorMessages,
                        e.getStatusCode().toString(),
                        e.getClass().getName()
                )
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException e) {
        Throwable cause = getRootCauseMessage(e);
        logger.error("HttpMessageNotReadableException occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(
                        cause.getMessage(),
                        HttpStatus.BAD_REQUEST.toString(),
                        cause.getClass().getName()
                )
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleException(BadCredentialsException e) {
        logger.error("Bad credentials exception occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorResponse(
                        "Incorrect password entered",
                        HttpStatus.UNAUTHORIZED.toString(),
                        e.getClass().getName()
                )
        );
    }
}
