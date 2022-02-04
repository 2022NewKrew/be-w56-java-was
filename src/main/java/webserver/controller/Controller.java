package webserver.controller;

import http.request.HttpRequest;
import http.response.HttpResponse;

import java.io.IOException;

public interface Controller {
    String handleGet(HttpRequest request, HttpResponse response) throws IOException;
    String handlePost(HttpRequest request, HttpResponse response) throws IOException;
}
