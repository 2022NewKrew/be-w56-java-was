package user.controller;

import db.DataBase;
import http.CookieManager;
import http.HttpCookie;
import http.Request;
import model.User;

import java.util.Map;

public class UserController {

    //회원가입
    public static String createUser(Request request) {
        Map<String, String> elements = request.getElements();

        User user = new User(elements.get("userId"),
                            elements.get("password"),
                            elements.get("name"),
                            elements.get("email"));

        DataBase.addUser(user);

        return "redirect:/index.html";
    }

    public static String loginUser(Request request){
        Map<String, String> elements = request.getElements();

        String userId = elements.get("userId");
        String password = elements.get("password");
        boolean canLogin = true;

        User user = DataBase.findUserById(userId);

        if(user == null){
            canLogin = false;
        }

        if(!user.isSamePassword(password)){
            canLogin = false;
        }

        String sessionId = request.getCookie().getValue("sessionId");
        CookieManager.addNewCookie(sessionId, "logined", (canLogin ? "true" : "false"));

        return (canLogin ? "redirect:/index.html" : "redirect:/user/login_failed.html");
    }
}
