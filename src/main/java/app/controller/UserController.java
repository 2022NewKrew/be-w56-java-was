package app.controller;

import app.db.DataBase;
import app.model.User;
import http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Cookie;
import webserver.http.RequestEntity;
import webserver.http.RequestParams;
import webserver.http.ResponseEntity;
import webserver.processor.handler.controller.Controller;
import webserver.processor.handler.controller.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController implements Controller {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Request(method = HttpMethod.POST, value = "/user/create")
    public ResponseEntity<?> createUserRequest(RequestEntity<Void> requestEntity) {
        List<Cookie> cookies = requestEntity.getCookies();
        RequestParams requestParams = requestEntity.getRequestParams();
        User user = createUser(requestParams);
        DataBase.addUser(user);
        HttpHeaders headers = new HttpHeaders(Map.of("Location", "/"));
        logger.info("User Created Id : {}, password : {}, name : {}, email : {}", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
        return new ResponseEntity<>(headers, List.of(), StatusCode.FOUND, "redirect:/");
    }

    @Request(method = HttpMethod.POST, value = "/user/login")
    public ResponseEntity<?> loginUser(RequestEntity<Void> requestEntity) {
        RequestParams requestParams = requestEntity.getRequestParams();
        String userId = requestParams.getParam("userId");
        String password = requestParams.getParam("password");
        User user = DataBase.findUserById(userId);
        HttpHeaders headers = new HttpHeaders(new HashMap<>());
        if(user.getPassword().equals(password)) {
            Cookie cookie = new Cookie("logined", "true");
            return new ResponseEntity<>(headers, List.of(cookie), StatusCode.FOUND, "redirect:/");
        }
        return new ResponseEntity<>(headers, new ArrayList<>(), StatusCode.FOUND, "redirect:/user/login_failed.html");
    }

    private User createUser(RequestParams requestParams) {
        String userId = requestParams.getParam("userId");
        String password = requestParams.getParam("password");
        String name = requestParams.getParam("name");
        String email = requestParams.getParam("email");
        return new User(userId, password, name, email);
    }
}
