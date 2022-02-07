package webserver.http.request.handler;

import Controller.Controller;
import Controller.StaticFileController;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import webserver.http.request.HttpRequest;
import webserver.http.request.Method;
import webserver.http.request.exceptions.PageNotFoundException;
import webserver.http.response.HttpResponse;

public class GetMethodHandler implements MethodHandler {

    private static final Map<String, Controller> controllerMap = new HashMap<>();

    static {
        // GET Method Controller 추가
    }

    private final StaticFileController staticFileController = new StaticFileController();

    public void handle(HttpRequest request, HttpResponse response)
        throws IOException, PageNotFoundException {
        if (request.getMethod() != Method.GET) {
            response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
            throw new PageNotFoundException();
        }

        URI uri = request.getUri();
        Controller controller = getController(uri);

        if (controller == null) {
            staticFileController.process(request, response);
            return;
        }

        controller.process(request, response);
    }

    private Controller getController(URI uri) {
        return controllerMap.get(uri.getPath());
    }
}
