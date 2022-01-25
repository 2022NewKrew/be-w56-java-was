package controller.user;

import annotation.Controller;
import annotation.RequestMapping;
import db.DataBase;
import http.*;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final HttpRequest httpRequest;

    public UserController(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @RequestMapping(value = "/user", method = "GET")
    public HttpResponse createUser() {
        try {
            RequestLine requestLine = httpRequest.getRequestLine();
            Map<String, String> queryStringMap = requestLine.getQueryStringMap();
            User user = new User(queryStringMap.get("userId"), queryStringMap.get("password"), queryStringMap.get("name"), queryStringMap.get("email"));
            DataBase.addUser(user);

            File file = new File("./webapp/index.html");
            byte[] body = Files.readAllBytes(file.toPath());

            HttpHeader header = new HttpHeader();
            header.addHeader("Content-Type", "text/html");
            header.addHeader("Content-Length", String.valueOf(body.length));
            header.addHeader("Location", "/index.html");

            return new HttpResponse("HTTP/1.1", HttpStatus.FOUND, header, body);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new HttpResponse("HTTP/1.1", HttpStatus.NOT_FOUND, new HttpHeader());
        }
    }

}
