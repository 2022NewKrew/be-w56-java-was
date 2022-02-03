package controller;

import dto.UserLoginRequest;
import dto.UserSignupRequest;
import exception.UserNotFoundException;
import http.PostMapping;
import repository.UserRepository;
import service.UserService;

@Controller
public class UserController {

    private final UserRepository userRepository = new UserRepository();
    private final UserService userService = new UserService();

    @PostMapping("/user")
    public String signup(UserSignupRequest request) {
        userService.signup(request);
        return "redirect:/index.html";
    }

    @PostMapping("/user/login")
    public String login(UserLoginRequest request) {
        try {
            userService.login(request);
            return "redirect:/index.html";
        } catch (UserNotFoundException e) {
            return "redirect:/user/login_failed.html";
        }
    }
}
