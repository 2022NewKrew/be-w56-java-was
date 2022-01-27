package controller;

import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.IOException;

public interface Controller {

    HttpResponse service(HttpRequest request) throws IOException;
}
