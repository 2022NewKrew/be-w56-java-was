package bin.jayden.controller;

import bin.jayden.annotation.Controller;
import bin.jayden.annotation.GetMapping;
import bin.jayden.annotation.PostMapping;
import bin.jayden.annotation.RequestMapping;
import bin.jayden.http.MyHttpSession;
import bin.jayden.model.User;
import bin.jayden.service.UserService;

import java.io.IOException;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public String createUser(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        return userService.getUserAddResult(user);
    }

    @PostMapping("/login")
    public String login(String userId, String password, MyHttpSession session) {
        User user = userService.getLoginUser(userId, password);
        if (user != null) {
            session.addAttribute("sessionUser", user);
            return "redirect:/";
        }
        return "redirect:/user/login_failed.html";
    }

    @GetMapping("/list")
    public String list(MyHttpSession session) throws IOException {
        User user = (User) session.getAttribute("sessionUser");
        if (user == null) {
            return "redirect:/user/login.html";
        } else {
            return userService.getUserListHtml();
        }
    }
}
