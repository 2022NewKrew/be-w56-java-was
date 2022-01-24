package com.kakao.webserver;

import lombok.Getter;

@Getter
public class HttpRequestLine {
    private final String method;
    private final String version;
    private final String url;

    public HttpRequestLine(String requestLine) {
        String[] requestLineTokens = requestLine.split(" ");
        if (requestLineTokens.length != 3) {
            throw new IllegalArgumentException("The request line should have 3 tokens only.");
        }
        this.method = requestLineTokens[0];
        this.url = requestLineTokens[1];
        this.version = requestLineTokens[2];
    }
}
