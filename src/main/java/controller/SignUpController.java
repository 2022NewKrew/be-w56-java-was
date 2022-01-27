package controller;

import db.DataBase;
import http.request.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import model.User;
import util.HttpRequestUtils;

import java.util.Map;

public class SignUpController implements Controller {

    @Override
    public boolean isSupported(HttpRequest httpRequest) {
        if ("/user/create".equals(httpRequest.getPath()) && httpRequest.isMethod(HttpMethod.POST)) {
            return true;
        }

        return false;
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> param = HttpRequestUtils.parseQueryString(httpRequest.getBody());
        User user = new User(param.get("userId"), param.get("password"),
                param.get("name"), param.get("email"));

        DataBase.addUser(user);
        httpResponse.redirect("/");
    }
}
