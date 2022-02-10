package springmvc;

import springmvc.controller.Controller;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpStatus;

import java.util.Optional;

public class FrontController {

    public static void doService(HttpRequest httpRequest, HttpResponse httpResponse) {

        Optional<Controller> controller = HandlerMapping.getController(httpRequest);

        ModelAndView mav = handle(httpRequest, httpResponse, controller);

        ViewResolver.resolve(mav, httpResponse);

    }

    private static ModelAndView handle(HttpRequest httpRequest, HttpResponse httpResponse, Optional<Controller> controller) {
        if (controller.isPresent()) {
            return HandlerAdapter.handle(httpRequest, httpResponse, controller.get());
        }
        return new ModelAndView(httpRequest.getPath(), HttpStatus.OK);
    }
}
