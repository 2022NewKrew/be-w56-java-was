package controller;

import http.Request;
import http.Response;

import java.io.IOException;

public interface Controller {

    void makeResponse(Request request, Response response) throws IOException;
}
