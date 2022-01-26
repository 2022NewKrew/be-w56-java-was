package webserver.controller;

import com.sun.jdi.InvocationException;
import lombok.extern.slf4j.Slf4j;
import model.*;
import util.HttpRequestUtils;
import webserver.Response;
import webserver.service.UserService;
import webserver.web.HttpStatus;
import webserver.web.request.Request;
import webserver.web.request.Url;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class UserController implements Controller {

    private final Map<String, Method> supplyUrl = new HashMap<>();
    private final UserService userService = UserService.getInstance();

    public UserController() {
        try {
            supplyUrl.put("GET /user/create", this.getClass().getMethod("getJoinUser", Request.class));
            supplyUrl.put("POST /user/create", this.getClass().getMethod("postJoinUser", Request.class));
        } catch (NoSuchMethodException e) {
            log.error("{}", e.getMessage());
        }
    }

    @Override
    public boolean isSupply(Request request) {
        String target = request.getMethod().toString() + " " + request.getUrl().toString();
        return supplyUrl.keySet().stream().anyMatch(key -> key.equals(target));
    }

    @Override
    public Response handle(Request request, OutputStream out) throws IOException {
        Response response;
        try {
            String target = request.getMethod().toString() + " " + request.getUrl().toString();
            Optional<String> result = supplyUrl.keySet().stream()
                    .filter(key -> key.equals(target))
                    .findAny();
            log.info("matching = {}", result);
            supplyUrl.get(result.get()).invoke(this, request);
            log.info("{}", supplyUrl.get(result.get()).getName());

            response = new Response.ResponseBuilder(out)
                    .setStatus(HttpStatus.REDIRECT)
                    .setRedirectLocation("/index.html")
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new Response.ResponseBuilder(out).setStatus(HttpStatus.NOT_FOUND).build();
        }
        return response;
    }
    //InvocationTargetException | IllegalAccessException | NoSuchElementException e
    public User getJoinUser(Request request) {
        Map<String, String> parameters = request.getUrl().getParameters();
        parameters.keySet().stream().forEach(key -> {
            log.info("{} : {}", key, parameters.get(key));
        });
        UserId userId = new UserId(parameters.get("userId"));
        Name name = new Name(parameters.get("name"));
        Password password = new Password(parameters.get("password"));
        Email email = new Email(parameters.get("email"));
        User user = new User(userId, password, name, email);
        return userService.joinUser(user);
    }

    public User postJoinUser(Request request) {
        return null;
    }
}
