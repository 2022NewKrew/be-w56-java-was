package cafe.controller.user;

import cafe.controller.exception.IncorrectLoginUserException;
import cafe.db.DataBase;
import cafe.model.User;
import framework.annotation.Controller;
import framework.annotation.RequestMapping;
import framework.http.*;
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
    public HttpResponse createUser() throws IOException {
        User user = new User(httpRequest.getRequestBody("userId"), httpRequest.getRequestBody("password"),
                httpRequest.getRequestBody("name"), httpRequest.getRequestBody("email"));
        DataBase.addUser(user);

        File file = new File("./webapp/index.html");
        byte[] body = Files.readAllBytes(file.toPath());

        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);
        responseHeader.setLocation("/index.html");

        return new HttpResponse("HTTP/1.1", HttpStatus.FOUND, responseHeader, body);
    }

    @RequestMapping(value = "/user/login", method = "POST")
    public HttpResponse login() throws IncorrectLoginUserException, IOException {
        User user = DataBase.findUserById(httpRequest.getRequestBody("userId"));

        if (user == null || !user.isValidPassword(httpRequest.getRequestBody("password"))) {
            throw new IncorrectLoginUserException();
        }

        File file = new File("./webapp/index.html");
        byte[] body = Files.readAllBytes(file.toPath());

        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);
        responseHeader.setCookie("logined=true; Path=/");
        responseHeader.setLocation("/index.html");

        return new HttpResponse("HTTP/1.1", HttpStatus.FOUND, responseHeader, body);
    }

    @RequestMapping(value = "/user/login.html", method = "GET")
    public HttpResponse userLoginHtml() throws IOException {
        File file = new File("./webapp" + httpRequest.getPath());
        byte[] body = Files.readAllBytes(file.toPath());

        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        return new HttpResponse("HTTP/1.1", HttpStatus.OK, responseHeader, body);
    }

    @RequestMapping(value = "/user/login_failed.html", method = "GET")
    public HttpResponse userLoginFailedHtml() throws IOException {
        File file = new File("./webapp" + httpRequest.getPath());
        byte[] body = Files.readAllBytes(file.toPath());

        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        return new HttpResponse("HTTP/1.1", HttpStatus.OK, responseHeader, body);

    }


    @RequestMapping(value = "/user/form.html", method = "GET")
    public HttpResponse userSignupHtml() throws IOException {
        File file = new File("./webapp" + httpRequest.getPath());
        byte[] body = Files.readAllBytes(file.toPath());

        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        return new HttpResponse("HTTP/1.1", HttpStatus.OK, responseHeader, body);
    }

    @RequestMapping(value = "/user/profile.html", method = "GET")
    public HttpResponse userProfileHtml() throws IOException {
        File file = new File("./webapp" + httpRequest.getPath());
        byte[] body = Files.readAllBytes(file.toPath());

        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        return new HttpResponse("HTTP/1.1", HttpStatus.OK, responseHeader, body);
    }

    @RequestMapping(value = "/user/list.html", method = "GET")
    public HttpResponse userProfileListHtml() throws IOException {
        File file = new File("./webapp" + httpRequest.getPath());
        byte[] body = Files.readAllBytes(file.toPath());

        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        return new HttpResponse("HTTP/1.1", HttpStatus.OK, responseHeader, body);
    }
}
