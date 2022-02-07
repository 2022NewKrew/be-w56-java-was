package webserver;

import controller.Controller;
import controller.StaticController;
import controller.UserController;
import webserver.http.MIME;

import java.util.Arrays;

public class ControllerMapper {
    private static final UserController userController = new UserController();
    private static final StaticController staticController = new StaticController();

    public static Controller mapController(String path) {
        if (path.startsWith("/user")) {
            return userController;
        } else if (Arrays.stream(MIME.values()).anyMatch(mime -> mime.isExtensionMatch(path))) {
            return staticController;
        } else {
            throw new IllegalArgumentException("Controller corresponding to path not found");
        }
    }
}
