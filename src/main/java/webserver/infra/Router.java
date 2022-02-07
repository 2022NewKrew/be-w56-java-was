package webserver.infra;

import controller.BaseController;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.model.ModelAndView;

public class Router {

    private final Routes routes = new Routes();

    public Router() {}

    public ModelAndView route(HttpRequest request, HttpResponse response) {
        BaseController controller = routes.getController(request.getHttpMethod(), request.getUrl());
        return controller.process(request, response);
    }

}
