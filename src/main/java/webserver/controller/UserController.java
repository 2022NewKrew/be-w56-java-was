package webserver.controller;

import domain.db.DataBase;
import domain.model.User;
import domain.service.LoginService;
import webserver.http.domain.CookieConst;
import webserver.http.domain.HttpMethod;
import webserver.http.domain.MethodAndUrl;
import webserver.http.response.Model;

import java.util.List;


public class UserController extends Controller {
    private static LoginService loginService = new LoginService();

    public UserController() {
        runner.put(new MethodAndUrl(HttpMethod.GET, "/user/form"), (req, res) -> {
            res.setStatusCode(200, "Ok")
                    .setUrl("/user/form.html")
                    .forward();
        });

        runner.put(new MethodAndUrl(HttpMethod.POST, "/user/create"), (req, res) -> {
            loginService.join(req.getParams());
            res.setStatusCode(302, "Found")
                    .setUrl("/index.html")
                    .forward();
        });

        runner.put(new MethodAndUrl(HttpMethod.GET, "/user/list"), (req, res) -> {
            List<User> users = DataBase.findAll();
            Model model = res.getModel();
            model.addAttribute("users", users);
            res.setStatusCode(200, "Ok")
                    .setUrl("/user/list.html")
                    .forward();
        });
    }
}
