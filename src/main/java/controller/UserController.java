package controller;

import controller.request.Request;
import controller.response.Response;
import db.DataBase;
import model.User;
import util.HttpStatus;

/**
 * Created by melodist
 * Date: 2022-01-25 025
 * Time: 오후 4:22
 */
public class UserController implements WebController{

    @Override
    public Response process(Request request) {
        User user = new User(request.getQueryStringParams("userId"),
                request.getQueryStringParams("password"),
                request.getQueryStringParams("name"),
                request.getQueryStringParams("email"));
        DataBase.addUser(user);

        return new Response(HttpStatus.REDIRECT, "/index.html");
    }
}
