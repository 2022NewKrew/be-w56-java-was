package controller;

import db.DataBase;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.HttpResponseFactory;
import java.io.DataOutputStream;
import java.io.IOException;
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
    public HttpResponse run(HttpRequest request, DataOutputStream dos) throws IOException {
        Map<String, String> result = new HashMap<>();
        Map<String, String> queries = request.getQuery();
        DataBase.addUser(
                new User(queries.get("userId"), queries.get("password"), queries.get("name"),
                        queries.get("email")));
        result.put("url", "/index.html");
        result.put("status", "302");
        return HttpResponseFactory.getHttpResponse(result, new HashMap<>(), dos);
    }
}
