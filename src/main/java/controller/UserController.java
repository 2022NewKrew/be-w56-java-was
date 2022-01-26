package controller;

import annotation.Controller;
import annotation.PostMapping;
import webserver.request.Request;
import webserver.response.Response;

@Controller
public final class UserController {

    @PostMapping("/user/create")
    public static Response userAdd(Request request) {
        System.out.println("user add post~!!");
        return null;
    }
}
