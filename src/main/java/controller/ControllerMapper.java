package controller;

import http.HttpRequest;
import http.HttpResponse;
import java.util.Map;

public class ControllerMapper {

    private static final String USER_REQUEST_PATH = "/user";
    private static final String DEFAULT_REQUEST_PATH = "/";

    private final Map<String, Controller> controllerMap;

    public static ControllerMapper create() {
        return new ControllerMapper(Map.of(DEFAULT_REQUEST_PATH, StaticResourceController.create(),
            USER_REQUEST_PATH, UserController.create()));
    }

    public ControllerMapper(Map<String, Controller> controllerMap) {
        this.controllerMap = controllerMap;
    }

    public HttpResponse handleRequest(HttpRequest request) {
        HttpResponse response = null;
        if (request.getPath().startsWith(USER_REQUEST_PATH)) {
            response = handleInternal(controllerMap.get(USER_REQUEST_PATH), request);
        }

        if (response == null || response.hasError()) {
            response = handleInternal(controllerMap.get(DEFAULT_REQUEST_PATH), request);
        }
        return response;
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
