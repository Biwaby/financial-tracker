package com.biwaby.financialtracker.dto.response;

import java.util.List;

public record ObjectListResponse(
        String response,
        String code,
        List<Object> objects
) { }
