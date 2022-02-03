package webserver.controller;

import webserver.Request;
import webserver.Response;

import java.io.IOException;

public class StaticFileController{
    private static StaticFileController instance = new StaticFileController();

    private StaticFileController() {}

    public static StaticFileController getInstance() {
        return instance;
    }

    public String control(Request request, Response response) throws IOException {
        return request.getUri();
    }
}
