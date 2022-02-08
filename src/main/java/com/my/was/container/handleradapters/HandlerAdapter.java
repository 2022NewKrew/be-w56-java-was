package com.my.was.container.handleradapters;

import com.my.was.http.request.HttpRequest;
import com.my.was.http.response.HttpResponse;

public interface HandlerAdapter {

    boolean isSupported(Object handler);

    void handle(Object handler, HttpRequest httpRequest, HttpResponse httpResponse);
}
