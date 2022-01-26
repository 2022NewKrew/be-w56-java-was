package webserver.controller;

import webserver.Request;
import java.io.IOException;

public class StaticFileController implements Controller{
    @Override
    public String control(Request request) throws IOException {
        return request.getUri();
    }
}
