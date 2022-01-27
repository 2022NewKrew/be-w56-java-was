package webserver.infra;

import controller.BaseController;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;
import webserver.model.ModelAndView;
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

    public ModelAndView route(HttpRequest request, HttpResponse response) {
        BaseController controller = routes.getController(request.getHttpMethod(), request.getUrl());
        return controller.process(request, response);
    }

}
