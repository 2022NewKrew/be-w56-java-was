package application.config;

import application.controller.HomeController;
import application.controller.UserController;
import was.config.NioWebServerConfig;
import was.config.NioWebServerConfigRegistry;
import was.meta.UrlPath;

import static was.meta.HttpMethod.GET;
import static was.meta.HttpMethod.POST;

public class WebConfig implements NioWebServerConfig {

    @Override
    public void registerController(NioWebServerConfigRegistry registry) {
        final HomeController homeController = new HomeController();
        registry.addController(GET, UrlPath.HOME, homeController.home);

        final UserController userController = new UserController();
        registry.addController(POST, UrlPath.SIGN_UP, userController.signUp);
        registry.addController(GET, UrlPath.LOGIN_FORM, userController.loginForm);
        registry.addController(POST, UrlPath.LOGIN, userController.login);
    }
}
