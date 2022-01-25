package webserver.controller;

import webserver.Request;
import webserver.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticFileController implements Controller{
    @Override
    public void start(Request request, Response response) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + request.getUri()).toPath());
        response.writeHeader(body.length, 200);
        response.writeBody(body);
    }
}
