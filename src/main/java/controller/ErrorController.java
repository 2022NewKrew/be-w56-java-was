package controller;

import http.HttpRequest;
import http.HttpResponse;

import java.io.IOException;

public class ErrorController implements Controller {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        response.getResponse("/error.html");
    }
}
