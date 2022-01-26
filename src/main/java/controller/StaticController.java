package controller;

import http.Request;
import http.Response;

import java.io.IOException;

public class StaticController implements Controller{
    @Override
    public void makeResponse(Request request, Response response) throws IOException {
        response.staticResponse(request.getUrl());
    }
}
