package controller;

import lombok.extern.slf4j.Slf4j;
import model.RequestHeader;
import model.HtmlResponse;
import service.UserService;
import model.builder.*;

@Slf4j
public class RequestController {
    private static final UserService userService = UserService.getInstance();

    private RequestController() {

    }

    public static HtmlResponse controlRequest(RequestHeader requestHeader) throws Exception {
        String uri = requestHeader.getHeader("uri");
        String method = requestHeader.getHeader("method");
        log.info("CONTROL URI: " + uri);
        log.info("CONTROL METHOD: " + method);

        if (uri.equals("/user/create") && method.equals("POST")) {
            userService.save(requestHeader);
            return new UserCreateResponseBuilder().build(requestHeader);
        }

        if (uri.equals("/user/login") && method.equals("POST")) {
            return new UserLoginResponseBuilder().build(requestHeader);
        }

        if (uri.equals("/user/list") && method.equals("GET")) {
            return new UserListResponseBuilder().build(requestHeader);
        }

        if (uri.equals("/user/logout") && method.equals("GET")) {
            return new UserLogoutResponseBuilder().build(requestHeader);
        }

        return new NormalRequestResponseBuilder().build(requestHeader);
    }
}
