package webserver.processor;

import http.HttpRequest;
import http.HttpResponse;
import webserver.exception.ResourceNotFoundException;
import webserver.processor.controller.Controller;

import java.util.List;

public class HttpProcessor {

    private final List<Controller> controllers;

    public HttpProcessor(List<Controller> controllers) {
        this.controllers = controllers;
    }

    public HttpResponse process(HttpRequest httpRequest) {
        for(Controller controller : controllers) {
            if(controller.supports(httpRequest)) {
                return controller.process(httpRequest);
            }
        }
        throw new ResourceNotFoundException();
    }
}
