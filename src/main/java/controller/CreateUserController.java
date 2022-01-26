package controller;

import db.DataBase;
import factory.UserFactory;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import webserver.HttpMethod;

import java.util.Map;

public class CreateUserController implements Controller {



    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HttpMethod method = request.getRequestLine().getHttpMethod();

        if (method.isPOST()) {
            doPost(request, response);
        }
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        Map<String, String> params = request.getRequestParams().getParams();
        User user = UserFactory.create(params.get("userId"), params.get("password"),
                params.get("name"), params.get("email"));

        DataBase.addUser(user);
        response.sendRedirect302Header("/");
    }
}
