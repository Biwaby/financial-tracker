package com.biwaby.financialtracker.dto.response;

import lombok.Value;

import java.util.List;

@Value
public class ObjectListResponse {

    String response;
    String code;
    List<Object> objects;
}
