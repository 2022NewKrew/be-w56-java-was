package webserver.controller;

import webserver.Request;
import webserver.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticFileController implements Controller{
    @Override
    public String control(Request request) throws IOException {
        return request.getUri();
    }
}
