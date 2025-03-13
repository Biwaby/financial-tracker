package com.biwaby.financialtracker.exception;

import lombok.Getter;

@Getter
public class ResponseException extends RuntimeException {

    private final Integer statusCode;
    private final String message;

    public ResponseException(Integer statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }
}
