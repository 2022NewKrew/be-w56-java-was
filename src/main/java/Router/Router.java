package Router;

import controller.BasicController;
import controller.Controller;
import controller.UserController;
import model.Request;
import model.Response;
import static controller.Controller.USER;

public class Router {

    public static Response routing(Request request) {
        String urlPath = request.getUrlPath();
        Controller controller;

        // /user로 시작
        if (urlPath.startsWith(USER)) {
            controller = new UserController();
            return controller.routing(urlPath.substring(USER.length()), request);
        }

        // 나머지
        controller = new BasicController();
        return controller.routing(urlPath, request);
    }
}
