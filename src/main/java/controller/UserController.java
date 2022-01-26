package controller;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.util.Map;

public class UserController implements Controller {

    private static final Logger log;
    private static final UserController INSTANCE;
    private static final UserService USER_SERVICE;

    static {
        log = LoggerFactory.getLogger(UserController.class);
        INSTANCE = new UserController();
        USER_SERVICE = UserService.getInstance();
    }

    private UserController() {}

    public static UserController getInstance() {
        return INSTANCE;
    }

    @Override
    public String handleRequest(String requestMethod, String requestPath, Map<String, String> queryParams) {
        if(requestMethod.equals("POST")) {
            return this.doPost(requestPath);
        }

        return this.doGet(requestPath, queryParams);
    }

    @Override
    public String doPost(String requestPath) {
        return null;
    }

    @Override
    public String doGet(String requestPath, Map<String, String> queryParams) {
        String viewPath = null;
        if(requestPath.equals("/user/create")) {
            viewPath = registerUser(queryParams);
        }

        return viewPath;
    }

    private String registerUser(Map<String, String> queryParams) {
        try {
            USER_SERVICE.createUser(queryParams);
            return "/index.html";
        } catch(IllegalArgumentException e) {
            log.error("[ERROR] - {}", e.getMessage());
            return "/error.html";
        }
    }
}
