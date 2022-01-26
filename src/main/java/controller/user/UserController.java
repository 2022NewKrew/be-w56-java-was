package controller.user;

import annotation.Controller;
import annotation.RequestMapping;
import db.DataBase;
import http.HttpHeader;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final HttpRequest httpRequest;

    public UserController(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @RequestMapping(value = "/user", method = "POST")
    public HttpResponse createUser() {
        try {
            User user = new User(httpRequest.getRequestBody("userId"), httpRequest.getRequestBody("password"),
                    httpRequest.getRequestBody("name"), httpRequest.getRequestBody("email"));
            DataBase.addUser(user);

            File file = new File("./webapp/index.html");
            byte[] body = Files.readAllBytes(file.toPath());

            HttpHeader responseHeader = new HttpHeader();
            responseHeader.setContentType("text/html");
            responseHeader.setContentLength(body.length);
            responseHeader.setLocation("/index.html");

            return new HttpResponse("HTTP/1.1", HttpStatus.FOUND, responseHeader, body);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new HttpResponse("HTTP/1.1", HttpStatus.NOT_FOUND, new HttpHeader());
        }
    }

}
