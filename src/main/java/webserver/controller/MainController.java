package webserver.controller;

import webserver.http.domain.CookieConst;
import webserver.http.domain.HttpMethod;
import webserver.http.domain.MethodAndUrl;

public class MainController extends Controller{

    public MainController() {
        runner.put(new MethodAndUrl(HttpMethod.GET, "/"), (req, res) -> {
            res.setUrl("/index.html");
            res.forward();
        });
    }
}
