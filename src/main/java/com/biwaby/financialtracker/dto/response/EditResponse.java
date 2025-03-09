package com.biwaby.financialtracker.dto.response;

import lombok.Value;

@Value
public class EditResponse {

    String response;
    String code;
    Object before;
    Object after;
}
