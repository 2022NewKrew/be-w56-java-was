package controller;

import webserver.HttpMethod;
import webserver.HttpRequest;

import java.util.Map;

public class HandlerAdapter {

    public static String getView(HttpRequest httpRequest, Controller controller) {
        if (controller == null) {
            return httpRequest.getPath();
        }
        if (httpRequest.getMethod().equals(HttpMethod.GET)) {
            Map<String, String> param = httpRequest.getQueryString();
            return controller.doGet(param);
        }
        if (httpRequest.getMethod().equals(HttpMethod.POST)) {
            Map<String, String> param = httpRequest.getQueryString();
            Map<String, String> body = httpRequest.getBodyMap();
            return controller.doPost(param, body);
        }
        return "redirect:/";

    }
}
