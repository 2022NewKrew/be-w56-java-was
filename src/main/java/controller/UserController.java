package controller;

import DTO.SignUpDTO;
import service.UserService;
import webserver.Request;

import java.util.Map;

public class UserController {
    public static String signUp(Request request) {
        Map<String, String> query = request.getQuery();

        UserService.SignUp(new SignUpDTO(query.get("userId"),
                query.get("password"),
                query.get("name"),
                query.get("email")));

        return "/index.html";
    }
}
