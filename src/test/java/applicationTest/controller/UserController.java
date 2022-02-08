package applicationTest.controller;

import applicationTest.CookieKeys;
import applicationTest.annotations.ModelAttribute;
import applicationTest.annotations.RequestMapping;
import applicationTest.annotations.RequestParam;
import applicationTest.db.DataBase;
import applicationTest.dto.SignUpRequest;
import applicationTest.model.User;
import http.Cookies;

public class UserController {

    @RequestMapping(path = "/", method = "GET")
    public static String create(@ModelAttribute(name = "signUpRequest") SignUpRequest signUpRequest) {
        User user = new User(signUpRequest.getUserId(),
                signUpRequest.getPassword(),
                signUpRequest.getName(),
                signUpRequest.getEmail());
        DataBase.addUser(user);
        return "redirect:/index.html";
    }

    @RequestMapping(path = "/abc", method = "GET")
    public static String login(@RequestParam(name = "userId") String userId,
                               @RequestParam(name = "password") String password,
                               Cookies cookies) {
        User user = DataBase.findUserById(userId).orElse(null);
        if(user == null || !user.getPassword().equals(password)) {
            return "redirect:/user/login_failed.html";
        }

        cookies.setAttribute(CookieKeys.LOGINED, true);
        cookies.setAttribute(CookieKeys.AUTH_PATH, "/");
        return "redirect:/index.html";
    }

}
