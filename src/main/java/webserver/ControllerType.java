package webserver;

import controller.Controller;
import controller.StaticFileController;
import controller.UserCreateController;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ControllerType {

    STATIC_FILE_CONTROLLER(HttpMethod.GET, "", StaticFileController.getInstance()),
    USER_CREATE_CONTROLLER(HttpMethod.POST, "/user/create", UserCreateController.getInstance());
    public static final Logger log = LoggerFactory.getLogger(ControllerType.class);
    public static final Map<HttpMethod, Map<String, Controller>> methodMap;
    public static final Map<String, Controller> controllerMap = new ConcurrentHashMap<>();

    static {
        methodMap = Collections.synchronizedMap(new EnumMap<>(HttpMethod.class));

        for (ControllerType controllerType : ControllerType.values()) {
            methodMap
                    .get(controllerType.getMethod())
                    .put(controllerType.getPath(), controllerType.getController());
        }
    }

    public static Controller getControllerType(HttpMethod httpMethod, String path) {
        return methodMap
                .get(httpMethod)
                .get(path);
    }

    private final HttpMethod method;
    private final String path;
    private final Controller controller;

    ControllerType(HttpMethod method, String path, Controller controller) {
        this.method = method;
        this.path = path;
        this.controller = controller;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Controller getController() {
        return controller;
    }
}
