package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Controller.HttpController;
import webserver.http.Controller.StaticController;
import webserver.http.Controller.dynamic.AuthController;
import webserver.http.Controller.dynamic.UserController;
import webserver.http.request.HttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RequestMapper {

    private static final Logger log = LoggerFactory.getLogger(RequestMapper.class);

    private final List<HttpController> controllers = new ArrayList<>();
    private static RequestMapper INSTANCE;

    public static RequestMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RequestMapper();
        }
        return INSTANCE;
    }

    private RequestMapper() {
        controllers.add(new StaticController());
        controllers.add(new UserController());
        controllers.add(new AuthController());
    }

    public Optional<HttpController> getController(HttpRequest request) {
        return controllers.stream()
                .filter(controller -> controller.isValidRequest(request))
                .findAny();
    }

}
