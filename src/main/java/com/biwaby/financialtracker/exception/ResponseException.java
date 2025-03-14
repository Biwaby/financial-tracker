package com.biwaby.financialtracker.exception;

import lombok.Getter;

@Getter
public class ResponseException extends RuntimeException {

    private final Integer statusCode;
    private final String message;
    private String exceptionClassName;

    public ResponseException(Integer statusCode, String message, String exceptionClassName) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
        this.exceptionClassName = exceptionClassName;
    }

    public ResponseException(Integer statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }
}
