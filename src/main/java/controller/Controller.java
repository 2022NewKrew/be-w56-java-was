package controller;

import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;

public interface Controller {

    HttpResponse handle(HttpRequest httpRequest) throws IOException;
}
