package com.biwaby.financialtracker.dto.response;

import lombok.Value;

@Value
public class ErrorResponse {

    String response;
    String code;
    String error;
}
