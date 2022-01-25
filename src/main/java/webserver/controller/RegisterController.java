package webserver.controller;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import model.User;
import webserver.http.HttpMethod;
import webserver.service.UserService;

import java.util.HashMap;
import java.util.Map;

public class RegisterController implements HttpController{
    private static final UserService userService = new UserService();

    @Override
    public HttpResponse process(HttpRequest request) {
        try{
            if(request.getMethod() != HttpMethod.GET){
                return new HttpResponse(HttpStatus.NOT_FOUND, "/error");
            }

            Map<String, String> queryParameter = request.getQueryParameter();
            User user = new User(
                    queryParameter.get("userId"),
                    queryParameter.get("password"),
                    queryParameter.get("name"),
                    queryParameter.get("email")
            );
            userService.join(user);

            Map<String, String> responseHeader = new HashMap<>();
            responseHeader.put("Location", "/");
            return new HttpResponse(HttpStatus.FOUND, "/", responseHeader);
        } catch (IllegalArgumentException e){
            return new HttpResponse(HttpStatus.BAD_REQUEST, "/error");
        }
    }
}
