package controller;

import db.DataBase;
import http.request.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import model.User;
import util.HttpRequestUtils;

import java.util.Map;

public class LoginController implements Controller{
    @Override
    public boolean isSupported(HttpRequest httpRequest) {
        if ("/user/login".equals(httpRequest.getPath()) && httpRequest.isMethod(HttpMethod.POST)) {
            return true;
        }

        return false;
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> param = HttpRequestUtils.parseQueryString(httpRequest.getBody());
        User loginUser = DataBase.findUserById(param.get("userId"))
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 다릅니다."));

        if (loginUser.isCorrectPassword(param.get("password"))) {
            httpResponse.addCookieAttribute("logined", "true");
            httpResponse.redirect("/");
            return;
        }

        throw new IllegalArgumentException("아이디 또는 비밀번호가 다릅니다.");
    }
}
