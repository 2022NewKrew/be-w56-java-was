package controller;

import annotation.Controller;
import annotation.RequestMapping;
import model.Request;
import model.Response;
import service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

@Controller("/user")
public class UserController {
    private final UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    @RequestMapping(value = "/user/login", requestMethod = "POST")
    public Response loginRouting(Request request) throws SQLException, IOException {
        if (userService.isRightLogin(request)) {
            Response response = Response.of(request, "/index.html", Files.readAllBytes(new File("./webapp" + "/index.html").toPath()));
            response.setCookie("logined=true");
            return response;
        }
        Response response = Response.of(request, "/user/login_failed.html",Files.readAllBytes(new File("./webapp" + "/user/login_failed.html").toPath()));
        response.setCookie("logined=false");
        return response;
    }

    @RequestMapping(value = "/user/create", requestMethod = "POST")
    public Response createRouting(Request request) throws SQLException, IOException {
        userService.save(request);
        return Response.of(request, "/user/list",Files.readAllBytes(new File("./webapp" + "/user/list.html").toPath()));
    }

    @RequestMapping(value = "/user", requestMethod = "GET")
    public static Response defaultRouting(Request request) throws IOException {
        return Response.of(request,
                request.getUrlPath(),Files.readAllBytes(new File("./webapp" + request.getUrlPath()).toPath()));
    }

    @RequestMapping(value = "/user/list", requestMethod = "GET")
    public Response listRouting(Request request) throws IOException, SQLException {
        if(userService.isLoginState(request)) {
            byte[] body = userService.userListToByte();
            return Response.of(request, "/user/list.html", body);
        }
        return Response.of(request, "/user/login.html", Files.readAllBytes(new File("./webapp" + "/user/login.html").toPath()));
    }
}
