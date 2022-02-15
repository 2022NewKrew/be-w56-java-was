package webserver.controller;

import exception.IllegalCreateUserException;
import service.UserService;
import webserver.dto.UserRequest;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;

public class UserController implements Controller {
    UserService userService;

    public UserController() {
        this.userService = UserService.getInstance();
    }

    @Override
    public HttpResponse handle(HttpRequest httpRequest) throws IOException{
        try {
            String url = httpRequest.getUrlPath();
            if (url.equals("/user/create"))
                return HttpResponse.of(create(httpRequest), 302);
            return null;
        }
        catch(IllegalCreateUserException e){
            return exceptionResponse(e.getMessage());
        }
    }

    private String create(HttpRequest httpRequest) {
        UserRequest userRequest = UserRequest.from(httpRequest.getInfoMap());
        userService.createUser(userRequest);
        return "/index.html";
    }

    private HttpResponse exceptionResponse(String message){
        return HttpResponse.from(message);
    }
}
