package webserver.infra;

import webserver.model.HttpRequest;
import webserver.model.HttpResponse;
import webserver.model.Routes;

public class Router {

    private static final Router instance = new Router();
    private final Routes routes = new Routes();

    private Router() {}

    public static Router getInstance() {
        if (instance == null) {
            return new Router();
        }
        return instance;
    }

    public HttpResponse route(HttpRequest request) {
        return routes.getController(request.getHttpMethod(), request.getUrl())
                .process(request);
    }

}
