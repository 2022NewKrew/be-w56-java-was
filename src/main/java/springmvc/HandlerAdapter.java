package springmvc;

import springmvc.controller.Controller;
import webserver.HttpMethod;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.util.Map;

public class HandlerAdapter {

    public static String handle(HttpRequest httpRequest, HttpResponse httpResponse, Controller controller) {

        controller.doService(httpRequest, httpResponse);
        return "redirect:/";

    }
}
