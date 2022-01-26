package controller;

import db.DataBase;
import model.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpStatus;
import util.request.HttpRequestUtils;
import util.request.Request;
import util.request.RequestLine;
import util.response.Response;
import util.response.ResponseBuilder;
import util.response.ResponseException;
import webserver.RequestHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class UserController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final UserController userController = new UserController();

    public static UserController getUserController() {
        return userController;
    }

    @Override
    public Response view(Request request, String userUrl) throws IOException {
        log.debug("user url : {}", userUrl);
        RequestLine requestLine = request.getLine();
        String method = requestLine.getMethod();
        if (method.equals("POST") && userUrl.matches("/create")) {
            String requestBody = request.getBody();
            Map<String, String> parameters = HttpRequestUtils.parseQueryString(requestBody);
            return createUser(parameters);
        }
        return ResponseException.notFoundResponse();
    }

    private Response createUser(Map<String, String> parameters) throws IOException {
        try {
            UserId userId = new UserId(parameters.get("userId"));
            Password password = new Password(parameters.get("password"));
            Name name = new Name(parameters.get("name"));
            Email email = new Email(parameters.get("email"));
            DataBase.addUser(new User(userId, password, name, email));
        } catch (NullPointerException e) {
            log.error("create user error : {}",e.getMessage());
            // 400 Bad Request
            return ResponseException.inputErrorResponse();
        }
        DataBase.findAll().forEach(user -> log.debug("user : {}", user));
        return new ResponseBuilder().setHttpStatus(HttpStatus.FOUND)
                        .addHeader("location","/index.html")
                        .build();
    }
}
