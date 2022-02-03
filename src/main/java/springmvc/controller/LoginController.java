package springmvc.controller;

import springmvc.db.DataBase;
import model.User;

import java.util.Map;

public class LoginController implements Controller {

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
            User user = findUser(body.get("userId"));
            validatePassword(user, body.get("password"));
            sessionCookie.put("logined", "true");
            return "redirect:/index.html";
        } catch (Exception e) {
            sessionCookie.put("logined", "false");
            return "redirect:/user/login_failed.html";
        }
    }

    private User findUser(String userId) {
        return DataBase.findUserById(userId).orElseThrow(
                () -> new IllegalArgumentException("아이디 오류")
        );
    }

    private void validatePassword(User user, String password) {
        if (!user.matchPassword(password)) {
            throw new IllegalArgumentException("비밀번호 오류");
        }
    }
}
