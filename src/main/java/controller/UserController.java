package controller;

import annotation.Controller;
import annotation.RequestMapping;
import model.Request;
import model.Response;
import service.UserService;

@Controller("/user")
public class UserController {
    public UserController() {
    }

    @RequestMapping(value = "/user/login", requestMethod = "POST")
    public static Response loginRouting(Request request) {
        if (UserService.isRightLogin(request)) {
            Response response = Response.of(request, "/index.html");
            response.setCookie("logined=true");
            return response;
        }
        Response response = Response.of(request, "/user/login_failed.html");
        response.setCookie("logined=false");
        return response;
    }

    @RequestMapping(value = "/user/create", requestMethod = "POST")
    public static Response createRouting(Request request) {
        UserService.save(request);
        return Response.of(request, "/user/list.html");
    }

    @RequestMapping(value = "/user", requestMethod = "GET")
    public static Response defaultRouting(Request request) {
        return Response.of(request,
                request.getUrlPath());
    }
}
