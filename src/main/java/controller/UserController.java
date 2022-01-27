package controller;

import dto.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import viewresolver.PostViewResolver;

import java.io.DataOutputStream;
import java.util.Map;

public class UserController implements Controller {

    private static final Logger log;
    private static final UserController INSTANCE;
    private static final UserService USER_SERVICE;

    private static final PostViewResolver POST_VIEW_RESOLVER;

    static {
        log = LoggerFactory.getLogger(UserController.class);
        INSTANCE = new UserController();
        USER_SERVICE = UserService.getInstance();
        POST_VIEW_RESOLVER = PostViewResolver.getInstance();
    }

    private UserController() {}

    public static UserController getInstance() {
        return INSTANCE;
    }

    @Override
    public void handleRequest(RequestInfo requestInfo, DataOutputStream dos) {
        String requestMethod = requestInfo.getRequestMethod();

        switch(requestMethod) {
            case "POST":
                this.doPost(requestInfo, dos);
                break;
            default:
                this.doGet(requestInfo, dos);
        }
    }

    @Override
    public void doPost(RequestInfo requestInfo, DataOutputStream dos) {
        String requestPath = requestInfo.getRequestPath();
        Map<String, String> bodyParams = requestInfo.getBodyParams();
        switch(requestPath) {
            case "/user/create":
                registerUser(requestInfo, dos);
                break;
        }
    }

    private void registerUser(RequestInfo requestInfo, DataOutputStream dos) {
        Map<String, String> bodyParams = requestInfo.getBodyParams();
        try {
            USER_SERVICE.createUser(bodyParams);
            POST_VIEW_RESOLVER.response(requestInfo, "/index.html", dos);
        } catch(IllegalArgumentException e) {
            log.error("[ERROR] - {}", e.getMessage());
        }
    }
}
