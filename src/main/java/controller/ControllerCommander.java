package controller;

import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ControllerCommander {
    private static List<Controller> controllers = new ArrayList<>();

    static {
        controllers.add(new UserController());
        controllers.add(new MainController());
    }

    public static Controller findController(String url) throws NoSuchProviderException {
        return controllers.stream()
                .filter(controller -> controller.isSupport(url)).findFirst()
                .orElseThrow(() -> new NoSuchProviderException("404 Error"));
    }
}
