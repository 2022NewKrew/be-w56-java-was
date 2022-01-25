package com.kakao.webserver.controller;

import com.kakao.http.header.HttpHeader;
import com.kakao.http.header.LocationHeader;
import com.kakao.http.request.HttpRequest;
import com.kakao.http.response.HttpResponse;
import com.kakao.http.response.HttpStatus;
import com.kakao.model.User;
import com.kakao.service.UserService;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static com.kakao.webserver.WebServerConfig.DEFAULT_HTTP_VERSION;

public class UserController implements Controller {
    private final UserService userService = UserService.getInstance();

    @Override
    public boolean isValidPath(String path) {
        return path.matches("/user/create");
    }

    @Override
    public void handleRequest(HttpRequest request, OutputStream os) throws Exception {
        Map<String, String> queryMap = request.getUrl().getQueryMap();
        String userId = URLDecoder.decode(queryMap.get("userId"), StandardCharsets.UTF_8);
        String password = URLDecoder.decode(queryMap.get("password"), StandardCharsets.UTF_8);
        String name = URLDecoder.decode(queryMap.get("name"), StandardCharsets.UTF_8);
        String email = URLDecoder.decode(queryMap.get("email"), StandardCharsets.UTF_8);
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
