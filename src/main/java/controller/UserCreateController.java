package controller;

import controller.request.Request;
import controller.response.Response;
import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by melodist
 * Date: 2022-01-25 025
 * Time: 오후 4:22
 */
public class UserCreateController implements WebController{
    private static final Logger log = LoggerFactory.getLogger(UserCreateController.class);

    @Override
    public Response process(Request request) {
        User user = new User(request.getBody("userId"),
                request.getBody("password"),
                request.getBody("name"),
                request.getBody("email"));
        DataBase.addUser(user);

        log.debug("user: {}", user);

        Map<String, String> headers = new HashMap<>();
        headers.put("Location", "/index.html");

        return new Response.Builder()
                .redirect()
                .headers(headers)
                .build();
    }
}
