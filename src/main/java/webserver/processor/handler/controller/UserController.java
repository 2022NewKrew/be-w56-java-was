package webserver.processor.handler.controller;

import app.model.User;
import db.DataBase;
import http.*;

import java.util.Map;

public class UserController implements Controller {

    @Request(method = HttpMethod.POST, value = "/user/create")
    public HttpResponse handle(HttpRequest httpRequest) {
        User user = createUser(httpRequest);
        DataBase.addUser(user);
        HttpHeaders headers = new HttpHeaders(Map.of("Location", "/"));
        return new HttpResponse(headers, StatusCode.FOUND, null);
    }

    private User createUser(HttpRequest httpRequest) {
        HttpRequestParams params = httpRequest.getRequestParams();
        String userId = params.getRequestParamName("userId");
        String password = params.getRequestParamName("password");
        String name = params.getRequestParamName("name");
        String email = params.getRequestParamName("email");
        return new User(userId, password, name, email);
    }
}
