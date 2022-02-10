package webserver;

import controller.*;

import java.util.concurrent.ConcurrentHashMap;

public class RequestMapper {

    private static final ConcurrentHashMap<String, Controller> requestMap = new ConcurrentHashMap<>();

    static {
        requestMap.put("/user/create", new JoinController());
        requestMap.put("/user/login", new LoginController());
        requestMap.put("/user/list", new UserListController());
        requestMap.put("/memo/create", new MemoController());
        requestMap.put("/", new IndexController());
    }

    public static Controller mapping(String url) {
        Controller controller = requestMap.get(url);
        if (controller == null) {
            controller = new StaticController();
        }
        return controller;
    }
}
