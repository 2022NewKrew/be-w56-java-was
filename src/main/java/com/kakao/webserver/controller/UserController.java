package com.kakao.webserver.controller;

import com.kakao.http.header.HttpHeader;
import com.kakao.http.header.LocationHeader;
import com.kakao.http.request.HttpMethod;
import com.kakao.http.request.HttpRequest;
import com.kakao.http.response.HttpResponse;
import com.kakao.http.response.HttpStatus;
import com.kakao.model.User;
import com.kakao.service.UserService;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static com.kakao.webserver.WebServerConfig.DEFAULT_HTTP_VERSION;

public class UserController implements HttpController {
    private final UserService userService = UserService.getInstance();

    @Override
    public boolean isValidRequest(HttpMethod method, String path) {
        return HttpMethod.POST.equals(method)
                && path.matches("/user/create");
    }

    @Override
    public void handleRequest(HttpRequest request, OutputStream os) throws Exception {
        String userId = request.findBodyParam("userId");
        String password = request.findBodyParam("password");
        String name = request.findBodyParam("name");
        String email = request.findBodyParam("email");
        User user = new User(userId, password, name, email);
        userService.addUser(user);
        response(os);
    }

    private void response(OutputStream os) throws IOException {
        List<HttpHeader> headers = List.of(new LocationHeader("/"));
        HttpResponse response = new HttpResponse(DEFAULT_HTTP_VERSION, HttpStatus.FOUND, headers);
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeBytes(response.toString());
        dos.flush();
    }
}
