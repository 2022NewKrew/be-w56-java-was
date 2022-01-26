package controller;

import model.Request;
import model.Response;
import service.UserService;

public class UserController {
    private UserController() {
        throw new IllegalStateException("Utility class");
    }

    public static Response routing(Request request) {
        String urlPath = request.getUrlPath();
        if (urlPath.equals("/user/create")) {
            return createRouting(request);
        }
        if (urlPath.equals("/user/login")) {
            return loginRouting(request);
        }
        return defaultRouting(request);
    }

    private static Response loginRouting(Request request) {
        if (isRightLogin(request)) {
            Response response = Response.of(request, "/index.html");
            response.setCookie("logined=true");
            return response;
        }
        Response response = Response.of(request, "/user/login_failed.html");
        response.setCookie("logined=false");
        return response;
    }

    private static Response createRouting(Request request) {
        save(request);
        return Response.of(request, "/user/list.html");
    }

    private static boolean isRightLogin(Request request) {
        return UserService.isRightLogin(request);
    }

    public static void save(Request request) {
        UserService.save(request);
    }

    private static Response defaultRouting(Request request) {
        return Response.of(request,
                request.getUrlPath());
    }
}
