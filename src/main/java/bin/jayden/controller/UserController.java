package bin.jayden.controller;

import bin.jayden.annotation.Controller;
import bin.jayden.annotation.PostMapping;
import bin.jayden.annotation.RequestMapping;
import bin.jayden.db.DataBase;
import bin.jayden.http.MyHttpRequest;
import bin.jayden.http.MyHttpSession;
import bin.jayden.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/create")
    public String createUser(MyHttpSession session, MyHttpRequest request) {
        Map<String, String> params = request.getParams();
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        DataBase.addUser(user);
        log.info("new User (userId : {}, name : {})", user.getUserId(), user.getName());
        return "redirect:/index.html";
    }

    @PostMapping("/login")
    public String login(MyHttpSession session, MyHttpRequest request) {
        Map<String, String> params = request.getParams();
        User user = DataBase.findUserById(params.get("userId"));
        if (user != null && user.getPassword().equals(params.get("password"))) {
            session.addAttribute("sessionUser", user);
            return "redirect:/index.html";
        }
        return "redirect:/user/login_failed.html";
    }
}
