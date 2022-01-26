package controller;

import http.HttpRequest;
import http.HttpResponse;

public class StaticController implements Controller {

    private final String filePath;

    public StaticController(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        response.forward(filePath);
    }
}
