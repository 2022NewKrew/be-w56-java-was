package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final DataBase dataBase = new DataBase();

    public void add(
            final Map<String, String> parameterMap
    )
    {
        final String id = parameterMap.get("id");

        if (Objects.nonNull(dataBase.findUserById(id))) {
            throw new IllegalStateException("User with id(" + id + ") already exist!");
        }

        final User user = new User(
                parameterMap.get("id"),
                parameterMap.get("password"),
                parameterMap.get("name"),
                parameterMap.get("email"));

        dataBase.addUser(user);
        log.info("New user added! - " + id);
    }

}
