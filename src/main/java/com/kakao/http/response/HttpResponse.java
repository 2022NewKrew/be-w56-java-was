package com.kakao.http.response;

import com.kakao.http.header.HttpHeader;

import java.util.List;

public class HttpResponse {
    private final String version;
    private final HttpStatus status;
    private final List<HttpHeader> headers;

    public HttpResponse(String version, HttpStatus status, List<HttpHeader> headers) {
        this.version = version;
        this.status = status;
        this.headers = headers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(new HttpStatusLine(version, status));
        for (HttpHeader header : headers) {
            sb.append(header);
        }
        sb.append("\r\n");
        return sb.toString();
    }
}
