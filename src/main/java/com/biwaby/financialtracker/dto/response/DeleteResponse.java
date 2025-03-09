package com.biwaby.financialtracker.dto.response;

import lombok.Value;

@Value
public class DeleteResponse {

    String response;
    String code;
    Object deleted;
}
