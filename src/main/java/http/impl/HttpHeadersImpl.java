package http.impl;

import http.HttpHeaders;

import java.util.Collections;
import java.util.Map;

class HttpHeadersImpl implements HttpHeaders {

    private Map<String, String> headers;

    public HttpHeadersImpl(Map<String, String> headers) {
        this.headers = Collections.unmodifiableMap(headers);
    }

    @Override
    public String getHeaderByName(String headerName) {
        return headers.get(headerName);
    }
}
