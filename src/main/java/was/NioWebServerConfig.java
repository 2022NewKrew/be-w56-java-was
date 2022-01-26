package was;

import controller.HomeController;
import controller.UserController;
import was.domain.controller.Controller;
import was.meta.UrlPath;

import java.util.HashMap;
import java.util.Map;

public class NioWebServerConfig {
    private final Map<UrlPath, Controller> controllers = new HashMap<>();

    public Map<UrlPath, Controller> getControllers() {
        return controllers;
    }

    public NioWebServerConfig() {
        final HomeController homeController = new HomeController();
        controllers.put(UrlPath.HOME, homeController.home);

        final UserController userController = new UserController();
        controllers.put(UrlPath.SIGN_UP, userController.signUp);
        controllers.put(UrlPath.LOGIN_FORM, userController.loginForm);
        controllers.put(UrlPath.LOGIN, userController.login);
    }
}
