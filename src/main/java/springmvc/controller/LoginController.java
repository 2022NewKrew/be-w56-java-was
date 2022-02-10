package springmvc.controller;

import springmvc.ModelAndView;
import springmvc.db.DataBase;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpStatus;

import java.util.Map;

public class LoginController extends Controller {

    @Override
    public ModelAndView doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (httpRequest.getCookies().getOrDefault("logined", "false").equals("true")){
            return new ModelAndView("/index.html", HttpStatus.FOUND);
        }
        return new ModelAndView("/user/login.html", HttpStatus.FOUND);
    }

    @Override
    public ModelAndView doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            Map<String, String> body = httpRequest.getBody();
            User user = findUser(body.get("userId"));
            validatePassword(user, body.get("password"));
            httpResponse.addCookie("logined", "true");
            httpResponse.addCookie("Max-Age", "180"); // max-age 3분
            httpResponse.addCookie("Path", "/");
            return new ModelAndView("/", HttpStatus.FOUND);
        } catch (Exception e) {
            httpResponse.addCookie("logined", "false");
            httpResponse.addCookie("Max-Age", "180"); // max-age 3분
            httpResponse.addCookie("Path", "/");
            return new ModelAndView("/user/login_failed.html", HttpStatus.FOUND);
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
