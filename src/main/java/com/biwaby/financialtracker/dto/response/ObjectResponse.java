package com.biwaby.financialtracker.dto.response;

import lombok.Value;

@Value
public class ObjectResponse {

    String response;
    String code;
    Object object;
}
