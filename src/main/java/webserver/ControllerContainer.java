package webserver;

import controller.Controller;
import controller.MainController;
import controller.UserController;
import http.request.HttpRequest;
import http.response.HttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerContainer {
    private static final Map<String, Controller> controllerMap = new HashMap<>();

    static {
        controllerMap.put("user", UserController.getInstance());
    }

    /**
     * @param request  http 요청 객체
     *
     * url에서 key가 되는 값을 parsing (ex. /user/create -> user)
     * 이를 담당하는 controller가 존재하면 처리 위임
     * default controller = MainController
     */
    public static HttpResponse map(HttpRequest request) throws IOException {
        if (request == null) {
            throw new IOException("HttpRequest is null. (failed to read request)");
        }
        String url = request.line().path();
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
