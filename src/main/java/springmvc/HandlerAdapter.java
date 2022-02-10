package springmvc;

import springmvc.controller.Controller;
import webserver.HttpMethod;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.util.Map;

public class HandlerAdapter {

    public static ModelAndView handle(HttpRequest httpRequest, HttpResponse httpResponse, Controller controller) {

        return controller.doService(httpRequest, httpResponse);

    }
}
