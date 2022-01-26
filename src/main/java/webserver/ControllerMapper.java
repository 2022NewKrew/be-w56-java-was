package webserver;

import controller.Controller;
import controller.MainController;
import controller.UserController;
import http.request.HttpRequest;
import http.response.HttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerMapper {
    private static final Map<String, Controller> controllerMap = new HashMap<>();

    static {
        controllerMap.put("user", UserController.getInstance());
    }

    public static HttpResponse map(HttpRequest request) throws IOException {
        String url = request.line().url();
        String[] tokens = url.split("/");
        String urlKey = "";

        if (tokens.length > 1)
            urlKey = tokens[1];

        if (!controllerMap.containsKey(urlKey))
            return MainController.getInstance().process(request); // Default Controller
        else
            return controllerMap.get(urlKey).process(request);
    }
}
