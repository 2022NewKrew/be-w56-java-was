package webserver.controller;

import domain.service.LoginService;
import webserver.http.domain.CookieConst;
import webserver.http.domain.HttpMethod;
import webserver.http.domain.MethodAndUrl;

public class UserController extends Controller {
    private static LoginService loginService = new LoginService();


    public UserController() {
        runner.put(new MethodAndUrl(HttpMethod.GET, "/user/form"), (req, res) -> {
            res.setStatusCode(200, "Ok");
            res.setUrl("/user/form.html");
            res.forward();
        });

        runner.put(new MethodAndUrl(HttpMethod.POST, "/user/create"), (req, res) -> {
            loginService.join(req.getParams());
            res.setStatusCode(302, "Found");
            res.setUrl("/index.html");
            res.redirect();
        });

        runner.put(new MethodAndUrl(HttpMethod.POST, "/user/login"), (req, res) -> {
            boolean isLogin = loginService.login(req.getParams());
            if(isLogin) {
                res.setStatusCode(302, "Found");
                res.setUrl("/index.html");
                res.addHeader("set-cookie", CookieConst.LOGIN_COOKIE + "=true; path=/; HttpOnly");
            } else {
                res.setStatusCode(302, "Found");
                res.setUrl("/user/login_failed.html");
            }
            res.redirect();
        });
    }


}
