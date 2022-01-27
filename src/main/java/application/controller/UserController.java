package application.controller;

import application.CookieKeys;
import application.db.DataBase;
import application.dto.SignUpRequest;
import application.model.User;
import http.Cookies;
import http.request.HttpRequest;
import org.checkerframework.checker.units.qual.C;

public class UserController {

    public static String create(SignUpRequest signUpRequest) {
        User user = new User(signUpRequest.getUserId(),
                signUpRequest.getPassword(),
                signUpRequest.getName(),
                signUpRequest.getEmail());
        DataBase.addUser(user);
        return "redirect:/index.html";
    }

    public static String login(String userId, String password, Cookies cookies) {
        User user = DataBase.findUserById(userId).orElse(null);
        if(user == null || !user.getPassword().equals(password)) {
            return "redirect:/user/login_failed.html";
        }

        cookies.setAttribute(CookieKeys.LOGINED, true);
        cookies.setAttribute(CookieKeys.AUTH_PATH, "/");
        return "redirect:/index.html";
    }

}
