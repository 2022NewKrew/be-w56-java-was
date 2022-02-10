package webserver.http.request.handler;

import Controller.Controller;
import Controller.CreateMemoController;
import Controller.LoginController;
import Controller.PrintBasicPageController;
import Controller.PrintUserListController;
import Controller.SignUpUserController;
import Controller.StaticFileController;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import webserver.http.request.HttpRequest;
import webserver.http.request.Method;
import webserver.http.request.exceptions.PageNotFoundException;
import webserver.http.response.HttpResponse;

public class MethodHandler {

    private static final Map<Pair, Controller> controllerMap = new HashMap<>();

    static {
        controllerMap.put(new Pair(Method.POST, "/user/create"), new SignUpUserController());
        controllerMap.put(new Pair(Method.POST, "/user/login"), new LoginController());
        controllerMap.put(new Pair(Method.GET, "/user/list"), new PrintUserListController());
        controllerMap.put(new Pair(Method.POST, "/memo/create"), new CreateMemoController());
        controllerMap.put(new Pair(Method.GET, "/"), new PrintBasicPageController());
    }

    private final StaticFileController staticFileController = new StaticFileController();

    public void handle(HttpRequest request, HttpResponse response)
        throws IOException, PageNotFoundException, SQLException, ClassNotFoundException {
        if (!Method.contain(request.getMethod())) {
            response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
            throw new PageNotFoundException();
        }

        Method method = request.getMethod();
        URI uri = request.getUri();
        Controller controller = getController(method, uri);

        if (controller != null) {
            controller.process(request, response);
            return;
        }

        if (request.getMethod() == Method.POST) {
            throw new PageNotFoundException();
        }

        staticFileController.process(request, response);
    }

    private Controller getController(Method method, URI uri) {
        Pair pair = new Pair(method, uri.getPath());
        return controllerMap.get(pair);
    }

    private static class Pair {

        private final Method method;
        private final String uri;

        public Pair(Method method, String uri) {
            this.method = method;
            this.uri = uri;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Pair pair = (Pair) o;
            return method == pair.method && uri.equals(pair.uri);
        }

        @Override
        public int hashCode() {
            return Objects.hash(method, uri);
        }
    }
}
