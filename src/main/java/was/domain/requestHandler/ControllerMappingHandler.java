package was.domain.requestHandler;

import was.domain.controller.Controller;
import was.domain.controller.StaticResourceController;
import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;
import was.meta.UrlPath;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class ControllerMappingHandler implements RequestHandler {
    private Map<UrlPath, Controller> requestHandlers;
    private final RequestHandler staticResourceControllerHandler = StaticResourceController.getInstance();

    private ControllerMappingHandler() {
    }

    public static ControllerMappingHandler getInstance(Map<UrlPath, Controller> requestHandlers) {
        final ControllerMappingHandler instance = ControllerMappingHandlerWrapper.INSTANCE;
        instance.requestHandlers = requestHandlers;
        return instance;
    }

    private static class ControllerMappingHandlerWrapper {
        private static final ControllerMappingHandler INSTANCE = new ControllerMappingHandler();
    }

    @Override
    public void handle(HttpRequest req, HttpResponse res) throws IOException {
        final UrlPath reqUrlPath = UrlPath.findByMethodAndPath(req);

        final Controller controller = requestHandlers.get(reqUrlPath);

        if (Objects.isNull(controller)) {
            staticResourceControllerHandler.handle(req, res);
        } else {
            controller.handle(req, res);
        }
    }
}
