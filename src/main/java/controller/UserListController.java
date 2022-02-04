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
import util.MapUtil;

public class UserListController implements Controller {

    private static UserListController instance;

    public static synchronized UserListController getInstance() {
        if (instance == null) {
            instance = new UserListController();
        }

        return instance;
    }

    @Override
    public HttpResponse run(HttpRequest request, DataOutputStream dos) {
        String logined = request.getCookie("logined");
        if (!Boolean.parseBoolean(logined)) {
            return HttpResponse.found(
                    "/user/login.html",
                    MapUtil.getEmptyCookieMap(),
                    dos
            );
        }

        List<User> users = new ArrayList<>(DataBase.findAll());
        Map<String, Object> model = Map.of("users", users);

        return HttpResponse.ok(
                request.getUrl(),
                model,
                MapUtil.getEmptyCookieMap(),
                dos
        );
    }
}
