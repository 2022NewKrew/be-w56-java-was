package com.my.was.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpHeaders {

    private Map<String, String> headers = new HashMap<>();

    public void addHeader(String headerName, String headerContent) {
        headers.put(headerName, headerContent);
    }

    public Optional<String> getHeader(String headerName) {
        return Optional.ofNullable(headers.get(headerName));
    }

    public Map<String, String> getHeaders() {
        return Map.copyOf(headers);
    }
}
