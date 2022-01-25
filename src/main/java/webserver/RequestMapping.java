package webserver;

import controller.Controller;
import controller.FrontController;
import java.util.HashMap;
import java.util.Map;

public class RequestMapping {

    private static final Map<String, Controller> CONTROLLERS = new HashMap<>();
    private static final String FRONT = "front";

    static {
        CONTROLLERS.put(FRONT, new FrontController());
    }

    public Controller getController() {
        return CONTROLLERS.get(FRONT);
    }
}
