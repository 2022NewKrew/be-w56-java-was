package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FrontController {

    private static final Logger log;
    private static final Map<String, Controller> REQUEST_MAPPINGS;
    private static final FrontController INSTANCE;
    private static final ViewController DEFAULT_CONTROLLER;

    static {
        log = LoggerFactory.getLogger(FrontController.class);
        REQUEST_MAPPINGS = new HashMap<>();
        INSTANCE = new FrontController();
        DEFAULT_CONTROLLER = ViewController.getInstance();
        init();
    }

    private FrontController() {}

    private static void init() {
        REQUEST_MAPPINGS.put("/user/create", UserController.getInstance());
    }

    public static FrontController getInstance() {
        return INSTANCE;
    }

    public void dispatch(String requestMethod, String requestPath, Map<String, String> queryParams, DataOutputStream dos) throws IOException {

        log.info("[{}], path - {}, queryParams - {}", requestMethod, requestPath, queryParams);

        Controller controller = REQUEST_MAPPINGS.get(requestPath);
        String viewPath = requestPath;
        if(controller != null) {
            viewPath = controller.handleRequest(requestMethod, requestPath, queryParams);
        }
        DEFAULT_CONTROLLER.viewResolve(viewPath, dos);
    }
}
