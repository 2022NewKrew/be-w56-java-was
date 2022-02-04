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
    public void handleGet(HttpRequest request, HttpResponse response) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());
        response.setBody(body);

        response.setHttpVersion("HTTP/1.1");
        response.setStatusCode(HttpStatusCode.OK);

        HttpHeader responseHeader = new HttpHeader();
        responseHeader.addHeader("Content-Type: text/html;charset=utf-8");
        responseHeader.addHeader("Content-Length: " + body.length);
        response.setHeader(responseHeader);
        response.send();
    }

    @Override
    public void handlePost(HttpRequest request, HttpResponse response) throws IOException {

    }
}
