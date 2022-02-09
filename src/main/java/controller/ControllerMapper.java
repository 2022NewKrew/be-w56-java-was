package controller;

import controller.exception.ControllerMismatchException;
import http.HttpRequest;
import http.HttpResponse;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerMapper {

    private static final Logger log = LoggerFactory.getLogger(ControllerMapper.class);

    private static final String AUTH_REQUEST_PATH = "/user/login";
    private static final String USER_REQUEST_PATH = "/user";
    private static final String DEFAULT_REQUEST_PATH = "/";

    private final Map<String, Controller> controllerMap;
    private final List<String> mappingPaths;
    private final StaticFileReader staticFileReader;

    public static ControllerMapper create() {
        Map<String, Controller> controllerMap = Map.of(
            DEFAULT_REQUEST_PATH, MemoController.create(),
            USER_REQUEST_PATH, UserController.create(),
            AUTH_REQUEST_PATH, AuthController.create());
        List<String> mappingPaths = List.of(AUTH_REQUEST_PATH, USER_REQUEST_PATH,
            DEFAULT_REQUEST_PATH);
        return new ControllerMapper(controllerMap, mappingPaths, StaticFileReader.create());
    }

    private ControllerMapper(Map<String, Controller> controllerMap,
        List<String> mappingPaths, StaticFileReader staticFileReader) {
        this.controllerMap = controllerMap;
        this.mappingPaths = mappingPaths;
        this.staticFileReader = staticFileReader;
    }

    public HttpResponse handleRequest(HttpRequest request) {
        try {
            return handleInternal(controllerMapping(request.getPath()), request);
        } catch (ControllerMismatchException e) {
            log.error("Controller Mismatched {} {}", e.getMessage(), e);
            return handleInternal(staticFileReader, request);
        }
    }

    private Controller controllerMapping(String path) {
        return controllerMap.get(mappingPaths.stream()
            .filter(path::startsWith)
            .findFirst()
            .orElse(DEFAULT_REQUEST_PATH));
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
