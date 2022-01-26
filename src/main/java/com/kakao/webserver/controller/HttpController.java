package com.kakao.webserver.controller;

import com.kakao.http.request.HttpMethod;
import com.kakao.http.request.HttpRequest;

import java.io.OutputStream;

public interface HttpController {
    boolean isValidRequest(HttpMethod method, String path);

    void handleRequest(HttpRequest request, OutputStream os)
            throws Exception;
}
