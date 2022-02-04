package webserver;

import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.ResponseBuilder;
import webserver.controller.Controller;
import webserver.controller.ControllerMap;

import java.io.File;
import java.io.IOException;
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
            handleFile(file ,request, response);
            return;
        }

        String path = controller.handleGet(request, response);
        ResponseBuilder.build(path,request,response);
    }

    private void handlePost(HttpRequest request, HttpResponse response) throws IOException {
        Controller controller = controllerMap.getController(request.getStartLine().getTargetUri());

        if(Objects.isNull(controller)){
            throw new IllegalArgumentException("wrong path: " + request.getStartLine().getTargetUri());
        }

        String path = controller.handlePost(request, response);
        ResponseBuilder.build(path,request,response);
    }

    private void handleFile(File file, HttpRequest request, HttpResponse response) throws IOException {
        if(!file.exists()){
            ResponseBuilder.build(null, request, response);
            return;
        }
        String path = file.getPath().replace("./webapp", "");
        ResponseBuilder.build(path, request, response);
    }
}
