package com.kakao.webserver.controller;

import com.kakao.http.request.HttpMethod;
import com.kakao.http.request.HttpRequest;
import com.kakao.http.response.HttpResponse;

public interface HttpController {
    boolean isValidRequest(HttpMethod method, String path);

    HttpResponse handleRequest(HttpRequest request)
            throws Exception;
}
