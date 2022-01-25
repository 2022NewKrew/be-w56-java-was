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
            save(request);
            return Response.of(request.getHttpMethod(),
                    request.getRespContextType(),
                    "/user/list.html");
        }

        return Response.of(request.getHttpMethod(),
                request.getRespContextType(),
                request.getUrlPath());
    }

    public static void save(Request request) {
        UserService.save(request);
    }
}
