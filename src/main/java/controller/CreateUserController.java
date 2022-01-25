package controller;

import db.DataBase;
import factory.UserFactory;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

import java.util.Map;

public class CreateUserController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        Map<String, String> params = request.getRequestParams().getParams();

        if (request.getRequestLine().getHttpMethod().equals("GET")) {
            User user = UserFactory.create(params.get("userId"), params.get("password"),
                    params.get("name"), params.get("email"));

            DataBase.addUser(user);
            response.sendRedirect302Header("/");
        }
    }
}
