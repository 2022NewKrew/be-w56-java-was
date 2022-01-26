package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.Controller;
import webserver.controller.StaticFileController;
import webserver.controller.user.UserGetCreateController;
import webserver.controller.user.UserPostCreateController;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HandlerMapper {
    private static final Logger log = LoggerFactory.getLogger(HandlerMapper.class);
    private static final Map<String, Controller> controllers = new HashMap<>(){{
        put("GET/users/create", new UserGetCreateController());
        put("POST/users", new UserPostCreateController());
    }};
    private static final Controller staticFileController = new StaticFileController();

    public String map(Request request) throws IOException {
        return controllers.getOrDefault(request.getMethod()+request.getUri(), staticFileController).control(request);
    }
}
