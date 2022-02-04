package cafe.controller.user;

import cafe.controller.exception.IncorrectLoginUserException;
import cafe.dto.LoginDto;
import cafe.dto.UserCreateDto;
import cafe.service.UserService;
import framework.annotation.Controller;
import framework.annotation.RequestBody;
import framework.annotation.RequestMapping;
import framework.http.enums.MediaType;
import framework.http.request.HttpRequest;
import framework.http.response.HttpResponse;
import framework.http.response.HttpResponseHeader;
import framework.http.response.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class UserController {
    private final UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    @RequestMapping(value = "/user", method = "POST")
    public HttpResponse createUser(@RequestBody UserCreateDto userCreateDto) throws IOException {
        userService.createUser(userCreateDto.getUserId(), userCreateDto.getPassword(), userCreateDto.getName(), userCreateDto.getEmail());

        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);
        responseHeader.setLocation("/index.html");

        return new HttpResponse(HttpStatus.FOUND, responseHeader);
    }

    @RequestMapping(value = "/user/login", method = "POST")
    public HttpResponse login(@RequestBody LoginDto loginDto) throws IncorrectLoginUserException, IOException {
        userService.authenticateUser(loginDto.getUserId(), loginDto.getPassword());
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);
        responseHeader.setCookie("logined=true; Path=/");
        responseHeader.setLocation("/index.html");

        return new HttpResponse(HttpStatus.FOUND, responseHeader);
    }

    @RequestMapping(value = "/user/login.html", method = "GET")
    public HttpResponse userLoginHtml(HttpRequest httpRequest) throws IOException {
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        return new HttpResponse(HttpStatus.OK, responseHeader, httpRequest.getPath());
    }

    @RequestMapping(value = "/user/login_failed.html", method = "GET")
    public HttpResponse userLoginFailedHtml(HttpRequest httpRequest) throws IOException {
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        return new HttpResponse(HttpStatus.OK, responseHeader, httpRequest.getPath());

    }


    @RequestMapping(value = "/user/form.html", method = "GET")
    public HttpResponse userSignupHtml(HttpRequest httpRequest) throws IOException {
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        return new HttpResponse(HttpStatus.OK, responseHeader, httpRequest.getPath());
    }

    @RequestMapping(value = "/user/profile.html", method = "GET")
    public HttpResponse userProfileHtml(HttpRequest httpRequest) throws IOException {
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
