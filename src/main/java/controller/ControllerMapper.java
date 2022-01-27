package controller;

import http.HttpRequest;
import http.HttpResponse;
import java.util.List;
import java.util.Map;

public class ControllerMapper {

    private static final String AUTH_REQUEST_PATH = "/user/login";
    private static final String USER_REQUEST_PATH = "/user";
    private static final String DEFAULT_REQUEST_PATH = "/";

    private final Map<String, Controller> controllerMap;
    private final List<String> mappingPaths;

    public static ControllerMapper create() {
        Map<String, Controller> controllerMap = Map.of(DEFAULT_REQUEST_PATH,
            StaticFileReader.create(),
            USER_REQUEST_PATH, UserController.create(),
            AUTH_REQUEST_PATH, AuthController.create());
        List<String> mappingPaths = List.of(AUTH_REQUEST_PATH, USER_REQUEST_PATH,
            DEFAULT_REQUEST_PATH);
        return new ControllerMapper(controllerMap, mappingPaths);
    }

    public ControllerMapper(Map<String, Controller> controllerMap,
        List<String> mappingPaths) {
        this.controllerMap = controllerMap;
        this.mappingPaths = mappingPaths;
    }

    public HttpResponse handleRequest(HttpRequest request) {
        Controller controller = controllerMapping(request.getPath());
        HttpResponse response = handleInternal(controller, request);

        if (response.hasError()) {
            response = handleInternal(controllerMap.get(DEFAULT_REQUEST_PATH), request);
        }
        return response;
    }

    private Controller controllerMapping(String path) {
        return controllerMap.get(mappingPaths.stream()
            .filter(path::startsWith)
            .findFirst()
            .orElseGet(() -> DEFAULT_REQUEST_PATH));
    }

    private HttpResponse handleInternal(Controller controller, HttpRequest request) {
        if (request.isGet()) {
            return controller.doGet(request);
        }
        if (request.isPost()) {
            return controller.doPost(request);
        }
        if (request.isPut()) {
            return controller.doPut(request);
        }
        if (request.isDelete()) {
            return controller.doDelete(request);
        }
        return controller.badRequest();
    }
}
