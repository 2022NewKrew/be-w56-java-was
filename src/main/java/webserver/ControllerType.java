package webserver;

import controller.Controller;
import controller.RootController;
import controller.StaticFileController;
import controller.UserCreateController;
import controller.UserListController;
import controller.UserLoginController;
import java.util.EnumMap;
import java.util.Map;
import util.MapUtil;

public enum ControllerType {

    STATIC_FILE_CONTROLLER(HttpMethod.GET, "", StaticFileController.getInstance()),
    USER_CREATE_CONTROLLER(HttpMethod.POST, "/user/create", UserCreateController.getInstance()),
    USER_LOGIN_CONTROLLER(HttpMethod.POST, "/user/login", UserLoginController.getInstance()),
    USER_LIST_CONTROLLER(HttpMethod.GET, "/user/list.html", UserListController.getInstance()),
    ROOT_CONTROLLER(HttpMethod.GET, "/", RootController.getInstance());

    public static final Map<HttpMethod, Map<String, Controller>> methodMap;

    static {
        methodMap = new EnumMap<>(HttpMethod.class);

        for (HttpMethod method : HttpMethod.values()) {
            methodMap.put(method, MapUtil.get(String.class, Controller.class));
        }

        for (ControllerType controllerType : ControllerType.values()) {
            methodMap
                    .get(controllerType.getMethod())
                    .put(controllerType.getPath(), controllerType.getController());
        }
    }

    public static Controller getControllerType(HttpMethod httpMethod, String path) {
        return methodMap
                .get(httpMethod)
                .getOrDefault(path, STATIC_FILE_CONTROLLER.getController());
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
