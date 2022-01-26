package controller;

import db.DataBase;
import http.request.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import model.User;

public class UserCreateController implements Controller {

    private static UserCreateController instance;

    public static synchronized UserCreateController getInstance() {
        if (instance == null) {
            instance = new UserCreateController();
        }
        return instance;
    }

    @Override
    public Map<String, String> run(HttpRequest request, Map<String, String> model) {
        Map<String, String> result = new HashMap<>();
        Map<String, String> queries = request.getQuery();
        DataBase.addUser(
                new User(queries.get("userId"), queries.get("password"), queries.get("name"),
                        queries.get("email")));
        result.put("url", "/index.html");
        result.put("status", "302");
        return result;
    }
}
