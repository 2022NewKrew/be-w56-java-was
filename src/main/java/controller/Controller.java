package controller;

import http.HttpRequest;
import http.HttpResponse;
import java.io.IOException;

public interface Controller {

    HttpResponse service(HttpRequest request) throws IOException;
}
