package com.biwaby.financialtracker.dto.response;

public record ErrorResponse(
        String response,
        String code,
        String error
) { }
