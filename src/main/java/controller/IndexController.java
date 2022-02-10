package controller;

import annotation.RequestMapping;
import http.request.HttpRequest;
import http.request.HttpRequestMethod;

public class IndexController extends Controller {

    @RequestMapping(value = "/", method = HttpRequestMethod.GET)
    public String index(HttpRequest httpRequest) {
        return "index";
    }
}
