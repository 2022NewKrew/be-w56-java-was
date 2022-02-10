package controller;

import annotation.Controller;
import annotation.CookieValue;
import annotation.RequestBody;
import annotation.RequestMapping;
import db.DataBase;
import http.HttpMethod;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Model;
import webserver.ModelAndView;

import java.util.Collection;
import java.util.Map;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/users")
    public ModelAndView listUsers(@CookieValue("logined") String logined) {
        Collection<User> users = DataBase.findAll();
        Model model = new Model();
        model.addAttribute("users", users);
        return new ModelAndView("webapp/user/list.html", model);
    }

    @RequestMapping(value = "/user/create", method = HttpMethod.POST)
    public ModelAndView createUser(@RequestBody Map<String, String> formData) {
        User user = new User(
                formData.get("userId"),
                formData.get("password"),
                formData.get("name"),
                formData.get("email")
        );
        DataBase.addUser(user);
        log.debug("Create user: {}", user);
        return new ModelAndView("redirect:/index.html");
    }

    @RequestMapping(value = "/user/login", method = HttpMethod.POST)
    public ModelAndView loginUser(
            @RequestBody Map<String, String> formData
    ) {
        String userId = formData.get("userId");
        String password = formData.get("password");
        Collection<User> users = DataBase.findAll();
        User user = users.stream()
                .filter(u -> u.getUserId().equals(userId))
                .filter(u -> u.getPassword().equals(password))
                .findAny()
                .orElse(null);

        ModelAndView mv = new ModelAndView();
        if (user == null) {
            mv.setViewName("webapp/user/login_failed.html");
            mv.addCookie("logined", "false");
        } else {
            mv.setViewName("redirect:/index.html");
            mv.addCookie("logined", "true");
        }
        return mv;
    }
}
