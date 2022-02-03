package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpMethod;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.util.Map;

public class HandlerAdapter {

    public static String handle(HttpRequest httpRequest, HttpResponse httpResponse, Controller controller) {
        if (controller == null) {
            return httpRequest.getPath();
        }

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
