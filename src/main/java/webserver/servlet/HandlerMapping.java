package webserver.servlet;

import app.AppConfig;
import app.RootController;
import app.user.adapter.in.ListUserController;
import app.user.adapter.in.LoginController;
import app.user.adapter.in.SignUpController;
import app.user.exception.HandlerNotFoundException;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerMapping {

    private final Logger logger = LoggerFactory.getLogger(HandlerMapping.class);
    private final HashMap<String, HttpControllable> map;

    private HandlerMapping() {
        AppConfig appConfig = new AppConfig();
        map = new HashMap<>();
        map.put(RootController.path, appConfig.rootController());
        map.put(SignUpController.path, appConfig.signUpController());
        map.put(LoginController.path, appConfig.loginController());
        map.put(ListUserController.path, appConfig.listUserController());
    }

    public static HandlerMapping getInstance() {
        return ApplicationContextHolder.INSTANCE;
    }

    public boolean hasMatchedHandler(String uri) {
        return map.containsKey(uri);
    }

    public HttpControllable getHandler(String uri) {
        if (!map.containsKey(uri)) {
            throw new HandlerNotFoundException(uri);
        }
        HttpControllable controller = map.get(uri);
        logger.info("Use " + controller.getClass() + " handler");
        return controller;
    }

    private static class ApplicationContextHolder {

        public static HandlerMapping INSTANCE = new HandlerMapping();
    }
}
