package controller;

import controller.request.Request;
import controller.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by melodist
 * Date: 2022-01-25 025
 * Time: 오후 4:22
 */
public class UserCreateController implements WebController{
    private static final Logger log = LoggerFactory.getLogger(UserCreateController.class);

    @Override
    public Response process(Request request) {
        String userId = request.getBody("userId");
        String password = request.getBody("password");
        String name = request.getBody("name");
        String email = request.getBody("email");
        UserService.addUser(userId, password, name, email);

        Map<String, String> headers = new HashMap<>();
        headers.put("Location", "/index.html");

        return new Response.Builder()
                .redirect()
                .headers(headers)
                .build();
    }
}
