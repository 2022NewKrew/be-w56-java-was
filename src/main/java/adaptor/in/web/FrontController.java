package adaptor.in.web;

import adaptor.in.web.exception.UriNotFoundException;
import adaptor.in.web.user.UserController;
import infrastructure.model.HttpRequest;
import infrastructure.model.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;

import static infrastructure.util.ResponseHandler.response400;

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

    public void handle(DataOutputStream dos, HttpRequest httpRequest) {
        Path path = httpRequest.getRequestPath();
        log.debug("Request Path: {}", path.getValue());
        try {
            if (path.getValue().equals("/")) {
                homeController.handle(dos, httpRequest);
            }
            if (path.getContentType().isDiscrete()) {
                staticResourceController.handleFileRequest(dos, httpRequest);
                return;
            }
            if (path.matchHandler("/user")) {
                userController.handle(dos, httpRequest);
            }
        } catch (UriNotFoundException e) {
            response400(dos);
        }
    }

}
