package controller;

import framework.util.RequestMapping;
import util.HttpRequest;
import util.HttpResponse;

public class UserController implements Controller{

    @RequestMapping(path = "/create", method = "GET")
    public String create(HttpRequest req, HttpResponse res) {
        return "/index.html";
    }
}
