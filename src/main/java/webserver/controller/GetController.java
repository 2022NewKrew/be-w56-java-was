package webserver.controller;

import network.HttpRequest;
import network.HttpResponse;
import network.Status;
import webserver.service.UserService;

import java.io.IOException;
import java.io.OutputStream;

public class GetController {

    private OutputStream outputStream;
    private UserService userService;

    public GetController(HttpRequest httpRequest, OutputStream outputStream){
        this.outputStream = outputStream;
        this.userService = new UserService(httpRequest);
    }

    public void handleGet(String path) throws IOException {
        Status status = Status.OK;
        HttpResponse.handleHtmlResponse(path, outputStream, status, false);
    }
}
