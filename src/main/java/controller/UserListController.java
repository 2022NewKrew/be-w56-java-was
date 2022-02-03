package controller;

import db.DataBase;
import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.User;
import util.ControllerUtils;

public class UserListController implements Controller{

    private static UserListController instance;

    public static synchronized UserListController getInstance() {
        if(instance == null) {
            instance = new UserListController();
        }

        return instance;
    }

    @Override
    public HttpResponse run(HttpRequest request, DataOutputStream dos) {
        List<User> users = new ArrayList<>(DataBase.findAll());
        Map<String, Object> model = new HashMap<>();

        String logined = request.getCookie("logined");

        if(Boolean.parseBoolean(logined)) {
            model.put("users", users);
            return HttpResponse.ok(
                    request.getUrl(),
                    model,
                    ControllerUtils.getEmptyCookieMap(),
                    dos
            );
        }

        return HttpResponse.found(
                "/user/login.html",
                ControllerUtils.getEmptyCookieMap(),
                dos
        );
    }
}
