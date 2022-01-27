package webserver;

import controller.Controller;
import controller.StaticController;
import controller.UserController;
import util.request.Request;
import util.request.RequestLine;
import util.response.Response;
import util.response.ResponseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerMapper {

    private static final ControllerMapper controllerMapper = new ControllerMapper();
    private final Map<String, Controller> controllerMap = new HashMap<>();

    private ControllerMapper() {
        controllerMap.put("static",StaticController.getController());
        controllerMap.put("user",UserController.getUserController());
    }

    public Response mapping(Request request) throws IOException {
        RequestLine requestLine = request.getLine();
        String url = requestLine.getUrl();
        // Static File
        if (url.matches(".+\\.(html|css|js|woff|ttf|ico)$")) {
            return controllerMap.get("static").view(request, url);
        }
        // Controller Mapping
        String key = url.split("/")[1];
        try {
            return controllerMap.get(key).view(request,url.split("/" + key)[1]);
        } catch (NullPointerException e) {
            // 404 Not Found
            return ResponseException.notFoundResponse();
        }

    }

    public static ControllerMapper getControllerMapper() {
        return controllerMapper;
    }
}
