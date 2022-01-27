package controller;

import controller.request.Request;
import controller.response.Response;
import db.DataBase;
import model.User;
import util.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by melodist
 * Date: 2022-01-25 025
 * Time: 오후 4:22
 */
public class UserCreateController implements WebController{

    @Override
    public Response process(Request request) {
        User user = new User(request.getQueryStringParams("userId"),
                request.getQueryStringParams("password"),
                request.getQueryStringParams("name"),
                request.getQueryStringParams("email"));
        DataBase.addUser(user);

        Map<String, String> headers = new HashMap<>();
        headers.put("Location", "/index.html");

        return new Response.Builder()
                .redirect()
                .headers(headers)
                .build();
    }
}
