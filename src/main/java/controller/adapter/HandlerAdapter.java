package controller.adapter;

import http.request.HttpRequest;
import http.response.HttpResponse;

public interface HandlerAdapter {
    boolean supports(HttpRequest request);

    void handle(HttpRequest request, HttpResponse response);
}
