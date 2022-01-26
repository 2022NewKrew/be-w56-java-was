package controller;

import dto.UserSignupRequest;
import http.PostMapping;

@Controller
public class UserController {

    @PostMapping("/user")
    public String signup(UserSignupRequest request) {
        return "redirect:/index.html";
    }
}
