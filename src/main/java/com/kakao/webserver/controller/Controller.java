package com.kakao.webserver.controller;

import com.kakao.http.request.HttpRequest;

import java.io.OutputStream;

public interface Controller {
    boolean isValidPath(String path);

    void handleRequest(HttpRequest request, OutputStream os);
}
