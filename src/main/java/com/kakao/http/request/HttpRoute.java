package com.kakao.http.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class HttpRoute {
    private final HttpMethod method;
    private final String path;

    public HttpRoute(HttpMethod method, String path) {
        this.method = method;
        this.path = path;
    }
}
