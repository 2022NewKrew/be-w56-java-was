package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.RequestHandler;

import java.util.Map;
import java.util.Optional;

public class Controller {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static Controller instance;
    private final RouterService routerService;

    private Controller() {
        routerService = new RouterService();
        init();
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
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
                res.setHeader("Set-Cookie", "logined=true; Path=/\r\n");
                res.redirect("/");
                return;
            }
            res.setHeader("Set-Cookie", "logined=false; Path=/\r\n");
            res.redirect("/");
        });

        routerService.get("/", (req, res) -> {
            log.info("regitered /");
            res.send("/index.html", req.getMIME());
        });
    }

    public Optional<Router> getURL(String method, String url) {
        return routerService.returnUrl(method, url);
    }
}
