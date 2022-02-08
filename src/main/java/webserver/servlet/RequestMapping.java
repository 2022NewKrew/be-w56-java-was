package webserver.servlet;

import app.AppConfig;
import app.RootController;
import app.user.adapter.in.LoginController;
import app.user.adapter.in.SignUpController;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestMapping {

    private final Logger logger = LoggerFactory.getLogger(RequestMapping.class);
    private final HashMap<String, HttpControllable> map;

    public RequestMapping() {
        AppConfig appConfig = new AppConfig();
        map = new HashMap<>();
        map.put(RootController.path, appConfig.rootController());
        map.put(SignUpController.path, appConfig.signUpController());
        map.put(LoginController.path, appConfig.loginController());
    }

    public static RequestMapping getInstance() {
        return ApplicationContextHolder.INSTANCE;
    }

    public HttpControllable getHandler(String uri) {
        if (!map.containsKey(uri)) {
            throw new NullPointerException("Handler not found. path: " + uri);
        }
        HttpControllable controller = map.get(uri);
        logger.info("Use " + controller.getClass() + " handler");
        return controller;
    }

    private static class ApplicationContextHolder {

        public static RequestMapping INSTANCE = new RequestMapping();
    }
}
