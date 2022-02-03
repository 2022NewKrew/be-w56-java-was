package webserver.controller;

import webserver.Request;
import java.io.IOException;

public class StaticFileController{
    private static StaticFileController instance = new StaticFileController();

    private StaticFileController() {}

    public static StaticFileController getInstance() {
        return instance;
    }

    public String control(Request request) throws IOException {
        return request.getUri();
    }
}
