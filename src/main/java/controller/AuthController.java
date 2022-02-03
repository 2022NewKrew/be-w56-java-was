package controller;

import db.DataBase;
import model.User;

import java.util.Map;

public class AuthController implements Controller{

    @Override
    public String doGet(Map<String, String> param, Map<String, String> sessionCookie) {
        if (sessionCookie.getOrDefault("logined", "false").equals("true")){
            return "redirect:/index.html";
        }
        return "redirect:/user/login.html";
    }

    @Override
    public String doPost(Map<String, String> param, Map<String, String> body, Map<String, String> sessionCookie) {
        try {
            User user = DataBase.findUserById(body.get("userId"));
            if (user == null) {
                throw new IllegalArgumentException("아이디 오류");
            }
            if (!user.matchPassword(body.get("password"))) {
                throw new IllegalArgumentException("비밀번호 오류");
            }
            sessionCookie.put("logined", "true");
            return "redirect:/index.html";
        } catch (Exception e) {
            sessionCookie.put("logined", "false");
            return "redirect:/user/login_failed.html";
        }
    }
}
