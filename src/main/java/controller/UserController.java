package controller;

import annotation.Auth;
import annotation.RequestMapping;
import db.DataBase;
import model.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;
import util.request.Request;
import util.response.Response;
import util.response.ResponseBuilder;
import util.response.ResponseException;
import webserver.RequestHandler;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Map;

public class UserController extends Controller {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final UserController userController = new UserController();

    public static UserController getUserController() {
        return userController;
    }

    @Override
    public Response view(Request request, String userUrl) throws IOException {
        log.debug("user url : {}", userUrl);
        return super.view(request, userUrl);
    }

    @RequestMapping(url = "/create", method = HttpMethod.POST)
    private Response createUser(Map<String, String> parameters) throws IOException {
        try {
            UserId userId = new UserId(parameters.get("userId"));
            Password password = new Password(parameters.get("password"));
            Name name = new Name(parameters.get("name"));
            Email email = new Email(parameters.get("email"));
            DataBase.addUser(new User(userId, password, name, email));
        } catch (NullPointerException e) {
            log.error("create user error : {}", e.getMessage());
            // 400 Bad Request
            return ResponseException.errorResponse(new Message("잘못 입력하였습니다.", "/user/form.html"), HttpStatus.BAD_REQUEST);
        }
        DataBase.findAll().forEach(user -> log.debug("user : {}", user));
        return new ResponseBuilder().setHttpStatus(HttpStatus.FOUND)
                .addHeader("location", "/index.html")
                .build();
    }

    @RequestMapping(url = "/login", method = HttpMethod.POST)
    private Response login(Map<String, String> parameters) {
        UserId userId = new UserId(parameters.get("userId"));
        Password password = new Password(parameters.get("password"));
        User user = DataBase.findUserById(userId);
        if (user == null || !user.getPassword().equals(password)) {
            return new ResponseBuilder().setHttpStatus(HttpStatus.FOUND)
                    .addHeader("location", "/user/login_failed.html")
                    .addHeader("Set-Cookie", "logined=false; Path=/")
                    .build();
        }
        return new ResponseBuilder().setHttpStatus(HttpStatus.FOUND)
                .addHeader("location", "/index.html")
                .addHeader("Set-Cookie", "logined=true; Path=/")
                .build();
    }

    @Auth
    @RequestMapping(url = "/list", method = HttpMethod.GET)
    private Response userList(@Nullable Map<String, String> parameters) throws IOException {
        Model model = new Model();
        model.setAttribute("users", DataBase.findAll());
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        String html = htmlBuilder.build("./webapp/user/list.html", model);
        byte[] body = html.getBytes();
        return new ResponseBuilder().setHttpStatus(HttpStatus.OK)
                .addHeader("Content-Type", "text/html" + ";charset=utf-8")
                .addHeader("Content-Length", String.valueOf(body.length))
                .setBody(body)
                .build();
    }
}
