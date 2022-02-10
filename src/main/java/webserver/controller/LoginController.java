package webserver.controller;

import domain.service.LoginService;
import webserver.http.domain.CookieConst;
import webserver.http.domain.HttpMethod;
import webserver.http.domain.MethodAndUrl;

public class LoginController extends Controller{
    private static LoginService loginService = new LoginService();

    public LoginController() {
        runner.put(new MethodAndUrl(HttpMethod.POST, "/user/login"), (req, res) -> {
            boolean isLogin = loginService.login(req.getParams());

            if(isLogin) {
                res.addHeader("set-cookie", CookieConst.LOGIN_COOKIE + "=true; path=/; HttpOnly");

                res.setStatusCode(302, "Found")
                        .setUrl("/index.html")
                        .forward();
            } else {
                res.setStatusCode(302, "Found")
                        .setUrl("/user/login_failed.html")
                        .forward();
            }
        });

        runner.put(new MethodAndUrl(HttpMethod.GET, "/user/logout"), (req, res) -> {
            res.addHeader("set-cookie", CookieConst.LOGIN_COOKIE + "=false; path=/; expires=Thu, 01 Jan 1970 00:00:01 GMT; HttpOnly");
            res.setStatusCode(302, "Found")
                    .setUrl("/index.html")
                    .forward();
        });
    }
}
