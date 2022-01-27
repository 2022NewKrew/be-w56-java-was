package was;

import application.controller.HomeController;
import application.controller.UserController;
import was.controller.Controller;
import was.meta.UrlPath;

import java.util.HashMap;
import java.util.Map;

public class ServerConfig {

    public final String host = "";

    public final int port = 8888;

    public final int bufferSize = 26214400;

    public final int threadSize = 0;

    public final Map<UrlPath, Controller> handlers = new HashMap<>();

    public ServerConfig() {
        addHandlers();
    }

    private void addHandlers() {
        final HomeController homeContoller = new HomeController();
        handlers.put(UrlPath.HOME, homeContoller.home);

        final UserController userController = new UserController();
        handlers.put(UrlPath.SIGNUP, userController.signup);
        handlers.put(UrlPath.LOGIN, userController.login);
    }
}
