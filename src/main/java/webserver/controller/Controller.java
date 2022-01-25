package webserver.controller;


import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.controller.request.HttpRequest;
import webserver.controller.response.HttpResponse;

public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    public static HttpResponse handle(HttpRequest httpRequest) {
        ServiceType serviceType = ServiceType.findService(httpRequest);
        Function<HttpRequest, HttpResponse> service = serviceType.getService();
        return service.apply(httpRequest);
    }
}
