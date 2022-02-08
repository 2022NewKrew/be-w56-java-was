package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequest;
import util.HttpRequestUtils;
import webserver.RequestHandler;

import java.util.Map;
import java.util.Optional;

public class BaseController {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final RouterService routerService;

    private BaseController() {
        routerService = new RouterService();
        init();
    }

    public static BaseController getInstance() {
        return LazyHolder.INSTANCE;
    }

    private void init() {
        routerService.post("/user/create", (req, res) -> {
            log.info("regitered /user/create");

            Map<String, String> userInfo = HttpRequestUtils.parseQueryString(req.getBody());
            User user = new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"), userInfo.get("email"));
            DataBase.addUser(user);
            log.info(user.toString());

            res.redirect("/");
        });

        routerService.post("/user/login", (req, res) -> {
            log.info("registerd /user/login");

            Map<String, String> userInfo = HttpRequestUtils.parseQueryString(req.getBody());
            User user = DataBase.findUserById(userInfo.get("userId"));
            if (user.isSameUser(userInfo.get("password"))) {
                res.setHeader("Set-Cookie", "logined=true; Path=/");
                res.redirect("/");
                return;
            }
            res.setHeader("Set-Cookie", "logined=false; Path=/");
            res.redirect("/");
        });

        routerService.get("/", (req, res) -> {
            log.info("regitered /");

            System.out.println(res);
            res.send("/index.html");
        });
    }

    public void route(HttpRequest request) {
        routerService.returnUrl(request.getMethod(), request.getUrl());
    }

    public Optional<Router> getURL(String method, String url) {
        return routerService.returnUrl(method, url);
    }

    private static class LazyHolder {
        private static final BaseController INSTANCE = new BaseController();
    }
}
