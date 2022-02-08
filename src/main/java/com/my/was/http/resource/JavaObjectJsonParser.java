package com.my.was.http.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JavaObjectJsonParser implements JavaObjectParser{

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getType() {
        return "application/json";
    }

    @Override
    public String parse(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("java object를 json으로 파싱하는데 실패했습니다", e);
        }
    }
}
