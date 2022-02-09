package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpResponseUtils;
import webserver.Router;
import webserver.request.Request;
import webserver.response.HttpStatusCode;
import webserver.response.Response;

import java.io.IOException;

public class StaticResourceController {
    private static final Logger log = LoggerFactory.getLogger(Router.class);

    private static StaticResourceController instance;

    private StaticResourceController() {}

    public static synchronized StaticResourceController getInstance() {
        if(instance == null) {
            instance = new StaticResourceController();
        }
        return instance;
    }

    public Response requestStaticResource(Request request) throws IOException {
        return HttpResponseUtils.createStaticResourceResponse(request, HttpStatusCode.OK);
    }
}
