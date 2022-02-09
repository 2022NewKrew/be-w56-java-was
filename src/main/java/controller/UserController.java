package controller;

import dto.HttpResponseStatus;
import dto.RequestInfo;
import exception.UnAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import viewresolver.GetViewResolver;
import viewresolver.PostViewResolver;

import java.io.DataOutputStream;
import java.util.Map;

public class UserController implements Controller {

    private static final Logger log;
    private static final UserController INSTANCE;
    private static final UserService USER_SERVICE;

    private static final PostViewResolver POST_VIEW_RESOLVER;
    private static final GetViewResolver GET_VIEW_RESOLVER;

    static {
        log = LoggerFactory.getLogger(UserController.class);
        INSTANCE = new UserController();
        USER_SERVICE = UserService.getInstance();
        POST_VIEW_RESOLVER = PostViewResolver.getInstance();
        GET_VIEW_RESOLVER = GetViewResolver.getInstance();
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
    public void doGet(RequestInfo requestInfo, DataOutputStream dos) {
        String requestPath = requestInfo.getRequestPath();
        switch(requestPath) {
            case "/user/list":
                this.showUserList(requestInfo, dos);
                break;
        }
    }

    @Override
    public void doPost(RequestInfo requestInfo, DataOutputStream dos) {
        String requestPath = requestInfo.getRequestPath();
        switch(requestPath) {
            case "/user/create":
                this.registerUser(requestInfo, dos);
                break;
            case "/user/login":
                this.loginUser(requestInfo, dos);
                break;
        }
    }

    private void showUserList(RequestInfo requestInfo, DataOutputStream dos) {
        try {
            USER_SERVICE.checkAuthorization(requestInfo);
            String userListTableTemplate = USER_SERVICE.getUserListTableTemplate();
            GET_VIEW_RESOLVER.dynamicResponse(dos, requestInfo, userListTableTemplate);
        } catch(UnAuthorizedException uae) {
            log.error("[ERROR] - {}", uae.getMessage());
            GET_VIEW_RESOLVER.errorResponse("/index.html", requestInfo.getVersion(), dos);
        }
    }

    private void registerUser(RequestInfo requestInfo, DataOutputStream dos) {
        Map<String, String> bodyParams = requestInfo.getBodyParams();
        try {
            USER_SERVICE.createUser(bodyParams);
            String redirectHeader = "Location: /index.html";
            POST_VIEW_RESOLVER.response(dos, requestInfo, HttpResponseStatus.FOUND, redirectHeader);
        } catch(IllegalArgumentException e) {
            log.error("[ERROR] - {}", e.getMessage());

            GET_VIEW_RESOLVER.errorResponse("/error.html", requestInfo.getVersion(), dos);
        }
    }

    private void loginUser(RequestInfo requestInfo, DataOutputStream dos) {
        Map<String, String> bodyParams = requestInfo.getBodyParams();
        try {
            USER_SERVICE.loginUser(bodyParams);
            String redirectHeader = "Location: /index.html";
            String cookieHeader = "Set-Cookie: logged_in=true; Path=/";
            POST_VIEW_RESOLVER.response(dos, requestInfo, HttpResponseStatus.FOUND, redirectHeader, cookieHeader);
        } catch(IllegalArgumentException e) {
            log.error("[ERROR] - {}", e.getMessage());
            GET_VIEW_RESOLVER.errorResponse("/user/login_failed.html", requestInfo.getVersion(), dos);
        }
    }
}
