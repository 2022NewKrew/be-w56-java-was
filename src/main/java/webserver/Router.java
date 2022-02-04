package webserver;

import http.HttpHeader;
import http.HttpStatusCode;
import http.request.HttpRequest;
import http.response.HttpResponse;
import webserver.controller.Controller;
import webserver.controller.ControllerMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Router {

    private static final ControllerMap controllerMap = new ControllerMap();
    private static final Router instance = new Router();

    private Router() {}

    public static Router getInstance(){
        return instance;
    }

    public void route(HttpRequest request, HttpResponse response) throws IOException {
        switch (request.getStartLine().getMethod()){
            case "GET":
                handleGet(request, response);
                break;
            case "POST":
                handlePost(request, response);
                break;
        }

    }

    private void handleGet(HttpRequest request, HttpResponse response) throws IOException {
        Controller controller = controllerMap.getController(request.getStartLine().getTargetUri());

        if(Objects.isNull(controller)){
            File file = new File("./webapp" + request.getStartLine().getTargetUri());
            handleFile(file, response);
            return;
        }

        controller.handleGet(request, response);
    }

    private void handlePost(HttpRequest request, HttpResponse response) throws IOException {
        Controller controller = controllerMap.getController(request.getStartLine().getTargetUri());

        if(Objects.isNull(controller)){
            throw new IllegalArgumentException("wrong path: " + request.getStartLine().getTargetUri());
        }

        controller.handlePost(request, response);
    }

    private void handleFile(File file, HttpResponse response) throws IOException {
        if(!file.exists()){
            throw new IllegalArgumentException("file not exists: " + file.getPath());
        }

        byte[] body = Files.readAllBytes(file.toPath());
        response.setBody(body);

        response.setHttpVersion("HTTP/1.1");
        response.setStatusCode(HttpStatusCode.OK);

        HttpHeader responseHeader = new HttpHeader();
        //TODO: MIME TYPE 설정
        responseHeader.addHeader("Content-Type: text/html;charset=utf-8");
        responseHeader.addHeader("Content-Length: " + body.length);
        response.setHeader(responseHeader);
        response.send();
    }
}
