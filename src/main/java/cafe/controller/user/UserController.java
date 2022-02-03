package cafe.controller.user;

import cafe.controller.exception.IncorrectLoginUserException;
import cafe.db.DataBase;
import cafe.model.User;
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

        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);
        responseHeader.setLocation("/index.html");

        return new HttpResponse(HttpStatus.FOUND, responseHeader);
    }

    @RequestMapping(value = "/user/login", method = "POST")
    public HttpResponse login() throws IncorrectLoginUserException, IOException {
        User user = DataBase.findUserById(httpRequest.getRequestBody("userId"));

        if (user == null || !user.isValidPassword(httpRequest.getRequestBody("password"))) {
            throw new IncorrectLoginUserException();
        }

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

        return new HttpResponse(HttpStatus.OK, responseHeader, httpRequest.getPath());
    }
}
