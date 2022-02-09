package controller;

import dto.RequestInfo;
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
    private static final DefaultController DEFAULT_CONTROLLER;

    static {
        log = LoggerFactory.getLogger(FrontController.class);
        REQUEST_MAPPINGS = new HashMap<>();
        INSTANCE = new FrontController();
        DEFAULT_CONTROLLER = DefaultController.getInstance();
        init();
    }

    private FrontController() {}

    private static void init() {
        REQUEST_MAPPINGS.put("/user/create", UserController.getInstance());
        REQUEST_MAPPINGS.put("/user/login", UserController.getInstance());
        REQUEST_MAPPINGS.put("/user/list", UserController.getInstance());
    }

    public static FrontController getInstance() {
        return INSTANCE;
    }

    public void dispatch(RequestInfo requestInfo, DataOutputStream dos) throws IOException {

        String requestMethod = requestInfo.getRequestMethod();
        String requestPath = requestInfo.getRequestPath();
        Map<String, String> queryParams = requestInfo.getQueryParams();
        Map<String, String> bodyParams = requestInfo.getBodyParams();

        log.info("[{}], path - {}, params - {}", requestInfo.getRequestMethod(), requestInfo.getRequestPath(), requestMethod.equals("POST") ? bodyParams : queryParams);

        Controller controller = REQUEST_MAPPINGS.getOrDefault(requestPath, DEFAULT_CONTROLLER);

        controller.handleRequest(requestInfo, dos);
    }
}
