package com.biwaby.financialtracker.dto.response;

public record ObjectResponse(
        String response,
        String code,
        Object object
) { }
