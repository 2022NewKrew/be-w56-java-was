package controller;

import http.request.HttpRequest;
import http.response.HttpResponse;

import java.io.IOException;

public interface Controller {
    HttpResponse process(HttpRequest request) throws IOException;
}
