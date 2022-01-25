package webserver.controller;

import model.User;
import util.HttpRequestUtils;
import webserver.mapper.UserMapper;
import webserver.request.RequestInfo;
import webserver.service.UserService;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static webserver.handler.ResponseHandler.*;

public class UserController {
    private UserController() {}

    private static class InnerInstanceClazz {
        private static final UserController instance = new UserController();
    }

    public static UserController getInstance() {
        return InnerInstanceClazz.instance;
    }

    public static void mapping(RequestInfo requestInfo, Map<String, String> headerMap, DataOutputStream dos) throws IOException {
        if(requestInfo.getUrl().equals("/user/create"))
        {
            InnerInstanceClazz.instance.create(requestInfo,headerMap,dos);
        }
    }
    // /user/create
    private void create(RequestInfo requestInfo, Map<String, String> headerMap, DataOutputStream dos) throws IOException {
        Map<String, String> userMap = HttpRequestUtils.parseQueryString(requestInfo.getQueryString());
        UserService.getInstance().save(userMap);

        responseRedirect200Header(dos, "/user/list.html");
    }
}
