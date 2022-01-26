package adaptor.in.web;

import adaptor.in.web.exception.FileNotFoundException;
import adaptor.in.web.user.UserController;
import infrastructure.model.HttpRequest;
import infrastructure.model.HttpResponse;
import infrastructure.model.Path;
import infrastructure.util.HttpResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FrontController {

    private static final FrontController INSTANCE = new FrontController();
    private static final Logger log = LoggerFactory.getLogger(FrontController.class);
    private final StaticResourceController staticResourceController = StaticResourceController.getInstance();
    private final HomeController homeController = HomeController.getInstance();
    private final UserController userController = UserController.getInstance();

    private FrontController() {
    }

    public static FrontController getINSTANCE() {
        return INSTANCE;
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
