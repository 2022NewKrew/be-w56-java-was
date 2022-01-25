package controller;

import http.request.HttpRequest;
import http.response.HttpResponse;

public interface Controller {

    boolean isSupported(HttpRequest httpRequest);

    void handle(HttpRequest httpRequest, HttpResponse httpResponse);


}
