package Controller;

import java.util.Map;

import db.DataBase;
import model.User;

public class UserController {
    private final Map<String, String> parameters;
    private final DataBase dataBase;

    public UserController(String method, Map<String, String> parameters) {
        this.parameters = parameters;
        this.dataBase = new DataBase();
    }

    public String run(String method) {
        if (method.matches("/user/create*")) {
            return create();
        }
        return "index.html";
    }

    private String create() {
        User user = new User(
                parameters.get("userId"),
                parameters.get("password"),
                parameters.get("name"),
                parameters.get("email")
        );
        dataBase.addUser(user);

        return "index.html";
    }
}
