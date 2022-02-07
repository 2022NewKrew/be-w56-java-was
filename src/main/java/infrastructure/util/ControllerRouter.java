package infrastructure.util;

import adaptor.in.web.HomeController;
import adaptor.in.web.StaticResourceController;
import adaptor.in.web.exception.FileNotFoundException;
import adaptor.in.web.exception.UriNotFoundException;
import adaptor.in.web.user.UserController;
import infrastructure.exception.AuthenticationException;
import infrastructure.model.HttpRequest;
import infrastructure.model.HttpResponse;
import infrastructure.model.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ControllerRouter {

    private static final Logger log = LoggerFactory.getLogger(ControllerRouter.class);
    private final LoginFilter loginFilter;
    private final StaticResourceController staticResourceController;
    private final HomeController homeController;
    private final UserController userController;

    public ControllerRouter(LoginFilter loginFilter, StaticResourceController staticResourceController, HomeController homeController, UserController userController) {
        this.loginFilter = loginFilter;
        this.staticResourceController = staticResourceController;
        this.homeController = homeController;
        this.userController = userController;
    }

    public HttpResponse handle(HttpRequest httpRequest) throws IOException {
        Path path = httpRequest.getRequestPath();
        log.debug("Request Path: {}", path.getValue());

        if (notAuthenticated(httpRequest, path)) {
            return HttpResponseUtils.badRequest();
        }

        try {
            if (path.equals("/")) {
                return homeController.handle(httpRequest);
            }
            if (path.getContentType().isDiscrete()) {
                return staticResourceController.handleFileRequest(httpRequest);
            }
            if (path.matchHandler("/user")) {
                return userController.handle(httpRequest);
            }
        } catch (FileNotFoundException e) {
            return HttpResponseUtils.notFound();
        } catch (UriNotFoundException e) {
            return HttpResponseUtils.notFound();
        }
        return HttpResponseUtils.badRequest();
    }

    private boolean notAuthenticated(HttpRequest httpRequest, Path path) {
        try {
            if (LoginFilter.matchUrl(path.getValue())) {
                loginFilter.preHandle(httpRequest);
            }
        } catch (AuthenticationException e) {
            return true;
        }
        return false;
    }
}
