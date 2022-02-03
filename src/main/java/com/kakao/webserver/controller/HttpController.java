package com.kakao.webserver.controller;

import com.kakao.http.request.HttpRequest;
import com.kakao.http.request.HttpRoute;
import com.kakao.http.response.HttpResponse;

public interface HttpController {
    boolean isValidRoute(HttpRoute route);

    HttpResponse handleRequest(HttpRequest request)
            throws Exception;
}
