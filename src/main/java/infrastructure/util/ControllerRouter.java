package infrastructure.util;

import adaptor.in.web.HomeController;
import adaptor.in.web.StaticResourceController;
import adaptor.in.web.exception.FileNotFoundException;
import adaptor.in.web.user.UserController;
import infrastructure.model.HttpRequest;
import infrastructure.model.HttpResponse;
import infrastructure.model.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ControllerRouter {

    private static final Logger log = LoggerFactory.getLogger(ControllerRouter.class);
    private final StaticResourceController staticResourceController;
    private final HomeController homeController;
    private final UserController userController;

    public ControllerRouter(StaticResourceController staticResourceController, HomeController homeController, UserController userController) {
        this.staticResourceController = staticResourceController;
        this.homeController = homeController;
        this.userController = userController;
    }

    public HttpResponse handle(HttpRequest httpRequest) throws IOException {
        Path path = httpRequest.getRequestPath();
        log.debug("Request Path: {}", path.getValue());

        try {
            if (path.getValue().equals("/")) {
                return homeController.handle(httpRequest);
            }
            if (path.getContentType().isDiscrete()) {
                return staticResourceController.handleFileRequest(httpRequest);
            }
            if (path.matchHandler("/user")) {
                return userController.handleWithResponse(httpRequest);
            }
        } catch (FileNotFoundException e) {
            return HttpResponseUtils.notFound();
        }
        return HttpResponseUtils.badRequest();
    }
}
