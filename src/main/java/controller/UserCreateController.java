package controller;

import controller.request.Request;
import controller.response.Response;
import service.UserService;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Created by melodist
 * Date: 2022-01-25 025
 * Time: 오후 4:22
 */
public class UserCreateController implements WebController{
    @Override
    public Response process(Request request) {
        String userId = request.getBody("userId");
        String password = request.getBody("password");
        String name = request.getBody("name");
        String email = request.getBody("email");
        UserService.addUser(userId, password, name, email);

        MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.add("Location", "/index.html");

        return new Response.Builder()
                .redirect()
                .headers(headers)
                .build();
    }
}
