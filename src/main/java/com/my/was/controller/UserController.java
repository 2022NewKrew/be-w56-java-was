package com.my.was.controller;

import com.my.was.db.DataBase;
import com.my.was.container.handlermappings.annotation.RequestMapping;
import com.my.was.http.request.HttpRequest;
import com.my.was.http.response.HttpResponse;
import com.my.was.model.User;
import com.my.was.renderer.UserListPageRenderer;
import com.my.was.util.HttpRequestUtils;
import com.my.was.container.handlermappings.annotation.Controller;

import java.util.Map;
import java.util.Optional;

@Controller
public class UserController {

    private final UserListPageRenderer userListPageRenderer = new UserListPageRenderer();

    @RequestMapping(value = "/user/create", method = "post")
    public void signUp(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> param = HttpRequestUtils.parseQueryString(httpRequest.getBody());
        User user = new User(param.get("userId"), param.get("password"),
                param.get("name"), param.get("email"));

        DataBase.addUser(user);
        httpResponse.redirect("/");
    }

    @RequestMapping(value = "/user/login", method = "post")
    public void login(HttpRequest httpRequest, HttpResponse httpResponse) {
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

    @RequestMapping(value = "/user/list", method = "get")
    public void getUserList(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (isNotLoginUser(getLoginedCookie(httpRequest))) {
            httpResponse.redirect("/user/login.html");
            return;
        }

        httpResponse.body(userListPageRenderer.makeUserListPage());
    }

    private Optional<String> getLoginedCookie(HttpRequest httpRequest) {
        Optional<String> logined = httpRequest.getCookie().
                flatMap((cookie -> cookie.getAttribute("logined")));
        return logined;
    }

    private boolean isNotLoginUser(Optional<String> logined) {
        return logined.filter("true"::equals).isEmpty();
    }
}
