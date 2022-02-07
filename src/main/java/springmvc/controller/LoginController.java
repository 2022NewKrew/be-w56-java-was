package springmvc.controller;

import springmvc.db.DataBase;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.util.Map;

public class LoginController extends Controller {

    @Override
    public String doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (httpRequest.getCookies().getOrDefault("logined", "false").equals("true")){
            return "redirect:/index.html";
        }
        return "redirect:/user/login.html";
    }

    @Override
    public String doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            Map<String, String> body = httpRequest.getBody();
            User user = findUser(body.get("userId"));
            validatePassword(user, body.get("password"));
            httpResponse.addCookie("logined", "true");
            return "redirect:/index.html";
        } catch (Exception e) {
            httpResponse.addCookie("logined", "false");
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
