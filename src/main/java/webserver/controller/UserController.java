package webserver.controller;

import webserver.request.RequestInfo;

import java.io.DataOutputStream;
import java.util.Map;

public class UserController {
    private UserController() {}

    private static class InnerInstanceClazz {
        private static final UserController instance = new UserController();
    }

    public static UserController getInstance() {
        return InnerInstanceClazz.instance;
    }

    public static void mapping(RequestInfo requestInfo, Map<String, String> headerMap, DataOutputStream dos) {
        if(requestInfo.getUrl().equals("/user/create"))
        {

        }
    }
    public void create(RequestInfo requestInfo, Map<String, String> headerMap, DataOutputStream dos)
    {

    }
}
