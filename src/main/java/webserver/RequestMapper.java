package webserver;

import controller.Controller;
import controller.DefaultController;
import controller.UserCreateController;

import java.util.HashMap;
import java.util.Map;

public class RequestMapper {

    private static final Map<String, Controller> requestMap = new HashMap<>();

    // url & 컨트롤러 쌍을 미리 등록해둔다.
    static {
        requestMap.put("/user/create", new UserCreateController());
    }

    public Controller mapping(String url) {
        // map 에서 url 에 맞는 controller 를 찾아주고
        Controller controller = requestMap.get(url);
        if (controller == null) {
            // 없으면 defaultController 를 찾아준다.
            controller = new DefaultController();
        }
        return controller;
    }

}
