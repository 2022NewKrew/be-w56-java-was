package com.kakao.webserver.controller;

import com.kakao.http.request.HttpMethod;
import com.kakao.http.request.HttpRequest;

import java.io.OutputStream;

public interface Controller {
    boolean isValidRequest(String path, HttpMethod method);

    void handleRequest(HttpRequest request, OutputStream os)
            throws Exception;
}
