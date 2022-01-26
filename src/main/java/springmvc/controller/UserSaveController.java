package springmvc.controller;

import springmvc.service.UserService;

import java.util.Map;

public class UserSaveController implements CustomController{
    private final UserService userService = new UserService();
    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        String userId = paramMap.get("userId");
        String password = paramMap.get("password");
        String name = paramMap.get("name");
        String email = paramMap.get("email");

        userService.userRegister(userId, password, name, email);

        return "/index";
    }
}
