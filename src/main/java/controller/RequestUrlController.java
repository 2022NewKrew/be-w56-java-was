package controller;

import controller.annotation.RequestMapping;
import db.DataBase;
import http.request.HttpRequest;
import http.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import model.User;

@Slf4j
public class RequestUrlController {

    @RequestMapping("/")
    public String index(HttpRequest request, HttpResponse response) {
        return "/index";
    }

    @RequestMapping(value = "/create", method = "POST")
    public String createUser(HttpRequest request, HttpResponse response) {
        String userId = request.getParam("userId");
        String password = request.getParam("password");
        String name = request.getParam("name");
        String email = request.getParam("email");
        User user = new User(userId, password, name, email);
        DataBase.addUser(user);

        log.info("user created = {}", user);
        return "redirect:/";
    }
}
