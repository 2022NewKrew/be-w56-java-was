package webserver;

import Controller.Controller;
import Controller.StaticFileController;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import webserver.http.request.HttpRequest;
import webserver.http.request.Method;
import webserver.http.response.HttpResponse;

public class GetMethodHandler implements MethodHandler {

    private static final Map<String, Controller> controllerMap = new HashMap<>();

    static {
        // POST Method Controller 추가
    }

    public String handle(HttpRequest request, HttpResponse response)
        throws IOException, IllegalAccessException {
        if (request.getMethod() != Method.GET) {
            response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
            throw new IllegalAccessException("잘못된 접근 입니다.");
        }

        URI uri = request.getUri();
        Controller controller = getController(uri);

        if (controller != null) {
            return controller.process(request, response);
        }

        return new StaticFileController().process(request, response);
    }

    private Controller getController(URI uri) {
        return controllerMap.get(uri.getPath());
    }
}
