package app.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.annotation.Controller;
import app.annotation.GetMapping;
import app.annotation.PostMapping;
import app.db.DataBase;
import app.model.user.User;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    // @RequestMapping(value = "/user/create", method = "GET")
    // public HttpResponse createUser(Map<String, String> query) {
    //     User user = new User(
    //             query.get("userId"),
    //             query.get("password"),
    //             query.get("name"),
    //             query.get("email"));
    //     DataBase.addUser(user);
    //     Map<String, String> headers = new HashMap<>();
    //     headers.put("Location", "/index.html");
    //     return new HttpResponse(headers, HttpStatus.Found);
    // }

    @GetMapping(value = "/")
    public String index() {
        return "/index.html";
    }

    @PostMapping(value = "/user")
    public String addUser(Map<String, String> body) {
        User user = new User(
                body.get("userId"),
                body.get("password"),
                body.get("name"),
                body.get("email")
        );
        DataBase.addUser(user);
        log.debug("add User: {} {} {} {}", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
        return "redirect:/index.html";
    }
}
