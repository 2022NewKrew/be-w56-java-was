package springmvc;

import springmvc.controller.Controller;
import webserver.HttpMethod;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.util.Map;

public class HandlerAdapter {

    public static String handle(HttpRequest httpRequest, HttpResponse httpResponse, Controller controller) {

        Map<String, String> sessionCookie = httpResponse.getCookies();
        if (httpRequest.getCookies().containsKey("logined")) {
            sessionCookie.put("logined", httpRequest.getCookies().get("logined"));
        }

        if (httpRequest.getMethod().equals(HttpMethod.GET)) {
            Map<String, String> param = httpRequest.getQueryString();
            return controller.doGet(param, sessionCookie);
        }
        if (httpRequest.getMethod().equals(HttpMethod.POST)) {
            Map<String, String> param = httpRequest.getQueryString();
            Map<String, String> body = httpRequest.getBodyMap();
            return controller.doPost(param, body, sessionCookie);
        }
        return "redirect:/";

    }
}
