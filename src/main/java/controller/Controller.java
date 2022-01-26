package controller;

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
            log.info(user.toString());

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
