package controller;

import annotation.GetMapping;
import annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import service.dto.EnrollUserCommand;

public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    @GetMapping(path = "/")
    public String getIndex() {
        return "/index.html";
    }

    @GetMapping(path = "/user/create")
    public String signUp(
            @RequestParam(name = "userId") String userId,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email) {

        UserService.enroll(new EnrollUserCommand(userId, password, name, email));

        return "redirect:/";
    }
}
