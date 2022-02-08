package webserver;

import controller.*;
import webserver.http.Url;
import webserver.request.Request;
import webserver.request.RequestLine;
import webserver.response.Response;
import webserver.response.ResponseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerMapper {

    private static final ControllerMapper controllerMapper = new ControllerMapper();
    private final Map<String, Controller> controllerMap = new HashMap<>();

    private ControllerMapper() {
        controllerMap.put("static", StaticController.getInstance());
        controllerMap.put("user", UserController.getInstance());
        controllerMap.put("", MainController.getInstance());
        controllerMap.put("memo", MemoController.getInstance());
    }

    public Response mapping(Request request) throws IOException {
        RequestLine requestLine = request.getLine();
        String url = requestLine.getUrl();
        // Static File
        if (url.matches(".+\\.(html|css|js|woff|ttf|ico)$")) {
            return controllerMap.get("static").view(request, url);
        }
        // Controller Mapping

        Url splitUrl = new Url(url);
        try {
            return controllerMap.get(splitUrl.getKey()).view(request,splitUrl.getNextUrl());
        } catch (NullPointerException e) {
            // 404 Not Found
            return ResponseException.notFoundResponse();
        }

    }

    public static ControllerMapper getControllerMapper() {
        return controllerMapper;
    }
}
