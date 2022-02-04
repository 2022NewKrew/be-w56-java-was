package webserver.controller;

import http.HttpHeader;
import http.HttpStatusCode;
import http.request.HttpRequest;
import http.response.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HomeController implements Controller{

    private static final Controller instance = new HomeController();

    private HomeController() {}

    public static Controller getInstance(){
        return instance;
    }

    @Override
    public String handleGet(HttpRequest request, HttpResponse response) throws IOException {
        if(request.getStartLine().getTargetUri().equals("/")) {
            return "/index.html";
        }

        return null;
    }

    @Override
    public String handlePost(HttpRequest request, HttpResponse response) throws IOException {
        return null;
    }
}
