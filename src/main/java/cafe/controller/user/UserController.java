package cafe.controller.user;

import cafe.controller.exception.IncorrectLoginUserException;
import cafe.service.UserService;
import framework.annotation.Controller;
import framework.annotation.RequestMapping;
import framework.http.enums.MediaType;
import framework.http.request.HttpRequest;
import framework.http.response.HttpResponse;
import framework.http.response.HttpResponseHeader;
import framework.http.response.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final HttpRequest httpRequest;
    private final UserService userService;

    public UserController(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
        this.userService = new UserService();
    }

    @RequestMapping(value = "/user", method = "POST")
    public HttpResponse createUser() throws IOException {
        userService.createUser(httpRequest.getRequestBody("userId"), httpRequest.getRequestBody("password"),
                httpRequest.getRequestBody("name"), httpRequest.getRequestBody("email"));

        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);
        responseHeader.setLocation("/index.html");

        return new HttpResponse(HttpStatus.FOUND, responseHeader);
    }

    @RequestMapping(value = "/user/login", method = "POST")
    public HttpResponse login() throws IncorrectLoginUserException, IOException {
        userService.authenticateUser(httpRequest.getRequestBody("userId"), httpRequest.getRequestBody("password"));
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);
        responseHeader.setCookie("logined=true; Path=/");
        responseHeader.setLocation("/index.html");

        return new HttpResponse(HttpStatus.FOUND, responseHeader);
    }

    @RequestMapping(value = "/user/login.html", method = "GET")
    public HttpResponse userLoginHtml() throws IOException {
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        return new HttpResponse(HttpStatus.OK, responseHeader, httpRequest.getPath());
    }

    @RequestMapping(value = "/user/login_failed.html", method = "GET")
    public HttpResponse userLoginFailedHtml() throws IOException {
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        return new HttpResponse(HttpStatus.OK, responseHeader, httpRequest.getPath());

    }


    @RequestMapping(value = "/user/form.html", method = "GET")
    public HttpResponse userSignupHtml() throws IOException {
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        return new HttpResponse(HttpStatus.OK, responseHeader, httpRequest.getPath());
    }

    @RequestMapping(value = "/user/profile.html", method = "GET")
    public HttpResponse userProfileHtml() throws IOException {
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        return new HttpResponse(HttpStatus.OK, responseHeader, httpRequest.getPath());
    }

    @RequestMapping(value = "/user/list.html", method = "GET")
    public HttpResponse userProfileListHtml() throws IOException {
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);
        String userListHtml = userService.getUserListHtml();

        return new HttpResponse(HttpStatus.OK, responseHeader, userListHtml.getBytes(StandardCharsets.UTF_8));
    }
}
