package springmvc;

import springmvc.controller.Controller;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.util.Optional;

public class FrontController {

    public static void doService(HttpRequest httpRequest, HttpResponse httpResponse) {

        Optional<Controller> controller = HandlerMapping.getController(httpRequest);

        String viewName = handle(httpRequest, httpResponse, controller);

        ViewResolver.resolve(viewName, httpResponse);

    }

    private static String handle(HttpRequest httpRequest, HttpResponse httpResponse, Optional<Controller> controller) {
        if (controller.isPresent()) {
            return HandlerAdapter.handle(httpRequest, httpResponse, controller.get());
        }
        return httpRequest.getPath();
    }
}
