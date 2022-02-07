package controller;

import db.DataBase;
import model.Pair;
import model.User;
import model.request.Body;
import model.request.Headers;
import model.request.HttpLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.SecurePassword;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private static final String LOCATION_USER_CREATE = "/user/create";
    private static final String LOCATION_USER_LOGIN = "/user/login";

    private final DataBase dataBase = new DataBase();

    public Headers process(final String location, final Body body) {
        final List<Pair> list = new ArrayList<>();
        if (LOCATION_USER_CREATE.equals(location)) {
            userCreate(list, body);
        }
        else if (LOCATION_USER_LOGIN.equals(location)) {
            userLogin(list, body);
        }

        return new Headers(list);
    }

    private void userCreate(final List<Pair> list, final Body body) {
        final String decodedParameters = URLDecoder.decode(body.get(), StandardCharsets.UTF_8);
        final Map<String, String> map = HttpRequestUtils.parseQueryString(decodedParameters);

        try {
            add(map);
        } catch (IllegalArgumentException e) {
            return;
        } catch (IllegalStateException e) {
            list.add(new Pair(Headers.HEADER_LOCATION, new HttpLocation("/user/dupId.html").getLocation()));
            return;
        }

        list.add(new Pair(Headers.HEADER_LOCATION, new HttpLocation("/index.html").getLocation()));
    }

    private void userLogin(final List<Pair> list, final Body body) {
        final String decodedParameters = URLDecoder.decode(body.get(), StandardCharsets.UTF_8);
        final Map<String, String> map = HttpRequestUtils.parseQueryString(decodedParameters);

        final User user = dataBase.findUserById(map.get("id"));
        if (user != null && SecurePassword.verify(user.getPassword(), map.getOrDefault("password", ""))) {
            list.add(new Pair(Headers.HEADER_LOCATION, new HttpLocation("/index.html").getLocation()));
            list.add(new Pair(Headers.HEADER_SET_COOKIE, "logined=true; Path=/"));
            return;
        }

        list.add(new Pair(Headers.HEADER_LOCATION, new HttpLocation("/user/login_failed.html").getLocation()));
        list.add(new Pair(Headers.HEADER_SET_COOKIE, "logined=false; Path=/"));
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
