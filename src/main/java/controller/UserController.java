package controller;

import db.DataBase;
import model.User;
import model.request.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private static final String LOCATION_USER_CREATE = "/user/create";

    private final DataBase dataBase = new DataBase();

    public String process(final String location, final Body body) {
        if (LOCATION_USER_CREATE.equals(location)) {
            return userCreate(body);
        }

        return "/";
    }

    private String userCreate(final Body body) {
        final String decodedParameters = URLDecoder.decode(body.get(), StandardCharsets.UTF_8);
        final Map<String, String> map = HttpRequestUtils.parseQueryString(decodedParameters);

        try {
            add(map);
        } catch (IllegalArgumentException e) {
            return "/error.html";
        } catch (IllegalStateException e) {
            return "/user/dupId.html";
        }

        return "/index.html";
    }

    private void add(final Map<String, String> parameterMap) {
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
