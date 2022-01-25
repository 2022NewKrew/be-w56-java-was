package controller;

import DTO.SignUpDTO;
import service.UserService;

import java.util.Map;

public class UserController {
    static String signUp(Map<String, String> query) {
        UserService.SignUp(new SignUpDTO(query.get("userId"),
                query.get("password"),
                query.get("name"),
                query.get("email")));
        return "/index.html";
    }
}
