package controller;

import http.HttpRequest;
import http.HttpResponse;
import service.Service;

import java.io.IOException;

public interface Controller {

    Service service = new Service();

    void makeResponse(HttpRequest request, HttpResponse response) throws IOException;
}
