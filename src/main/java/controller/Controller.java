package controller;

import http.Request;
import http.Response;

public interface Controller {
    String run(Request request, Response response);
}
