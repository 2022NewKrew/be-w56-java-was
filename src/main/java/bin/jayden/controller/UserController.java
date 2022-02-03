package bin.jayden.controller;

import bin.jayden.annotation.Controller;
import bin.jayden.annotation.GetMapping;
import bin.jayden.annotation.PostMapping;
import bin.jayden.annotation.RequestMapping;
import bin.jayden.db.DataBase;
import bin.jayden.http.MyHttpSession;
import bin.jayden.model.User;
import bin.jayden.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService = new UserService();

    @PostMapping("/create")
    public String createUser(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
        log.info("new User (userId : {}, name : {})", user.getUserId(), user.getName());
        return "redirect:/index.html";
    }

    @PostMapping("/login")
    public String login(String userId, String password, MyHttpSession session) {
        User user = DataBase.findUserById(userId);
        if (user != null && user.getPassword().equals(password)) {
            session.addAttribute("sessionUser", user);
            return "redirect:/index.html";
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
