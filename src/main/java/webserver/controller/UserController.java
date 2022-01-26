package webserver.controller;

import util.HttpRequestUtils;
import webserver.request.RequestBody;
import webserver.request.RequestMsg;
import webserver.request.RequestStartLine;
import webserver.service.UserService;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import static webserver.handler.ResponseHandler.*;

public class UserController {
    private UserController() {
    }

    private static class InnerInstanceClazz {
        private static final UserController instance = new UserController();
    }

    public static UserController getInstance() {
        return InnerInstanceClazz.instance;
    }

    public static void mapping(RequestMsg requestMsg, DataOutputStream dos) throws IOException {
        String url = requestMsg.getRequestStartLine().getUrl();
        String method = requestMsg.getRequestStartLine().getMethod();
        if (url.equals("/user/create") && method.equals("GET")) {
            InnerInstanceClazz.instance.get_create(requestMsg, dos);
        } else if (url.equals("/user/create") && method.equals("POST")) {
            InnerInstanceClazz.instance.post_create(requestMsg, dos);
        }

    }

    // /user/create
    private void get_create(RequestMsg requestMsg, DataOutputStream dos) throws IOException {
        Map<String, String> userMap = HttpRequestUtils.parseQueryString(requestMsg.getRequestStartLine().getQueryString());
        UserService.getInstance().save(userMap);

        responseRedirect302Header(dos, "/index.html");
    }

    // /user/create
    private void post_create(RequestMsg requestMsg, DataOutputStream dos) throws IOException {
        Map<String, String> userMap = HttpRequestUtils.parseQueryString(requestMsg.getRequestBody().getBody());
        UserService.getInstance().save(userMap);

        responseRedirect302Header(dos, "/index.html");
    }
}
