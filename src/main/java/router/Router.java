package router;

import controller.UserController;
import model.Request;
import model.Response;

public class Router {
    private Router() {
        throw new IllegalStateException("Utility class");
    }

    public static Response routing(Request request) {
        String urlPath = request.getUrlPath();
        if (urlPath.startsWith("/user")) {
            return UserController.routing(request);
        }

        return defaultRouting(request);
    }

    private static Response defaultRouting(Request request) {
        return Response.of(request, request.getUrlPath());
    }
}
