package app;

import template.Model;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.servlet.HttpControllable;

public class RootController implements HttpControllable {

    public static final String path = "/";

    @Override
    public String call(HttpRequest request, HttpResponse response, Model model) {
        return "/index";
    }
}
