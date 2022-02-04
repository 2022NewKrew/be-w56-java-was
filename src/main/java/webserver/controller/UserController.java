package webserver.controller;

import http.HttpHeader;
import http.HttpStatusCode;
import http.request.HttpRequest;
import http.response.HttpResponse;
import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class UserController implements Controller {
    private static final Controller instance = new UserController();

    private UserController() {
    }

    public static Controller getInstance() {
        return instance;
    }

    @Override
    public void handleGet(HttpRequest request, HttpResponse response) throws IOException {
        if(request.getStartLine().getTargetUri().equals("/user/create")){
            getUserForm(request, response);
        }
    }

    @Override
    public void handlePost(HttpRequest request, HttpResponse response) throws IOException {

    }

    private void getUserForm(HttpRequest request, HttpResponse response) throws IOException {
        Map<String, String> queryParams = request.getStartLine().getQueryParams();
        User user = new User(
                queryParams.get("userId"),
                queryParams.get("password"),
                queryParams.get("name"),
                queryParams.get("email")
                );

        byte[] body = {};
        response.setBody(body);

        response.setHttpVersion("HTTP/1.1");
        response.setStatusCode(HttpStatusCode.OK);

        HttpHeader responseHeader = new HttpHeader();
        responseHeader.addHeader("Content-Length: " + body.length);
        response.setHeader(responseHeader);
        response.send();
    }
}
