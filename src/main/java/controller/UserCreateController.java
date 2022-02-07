package controller;

import db.DataBase;
import java.io.IOException;
import java.util.Map;
import model.HttpRequest;
import model.HttpResponse;
import model.HttpStatus;
import model.ResponseFactory;
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
    public HttpResponse run(HttpRequest request) throws IOException {
        Map<String, String> queries = request.getQuery();
        User user = new User(queries.get("userId"), queries.get("password"), queries.get("name"), queries.get("email"));

        DataBase.addUser(user);
        return ResponseFactory.getResponse(request, HttpStatus.FOUND);
    }
}
