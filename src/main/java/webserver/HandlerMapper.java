package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.Controller;
import webserver.controller.StaticFileController;
import webserver.controller.UserCreateController;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HandlerMapper {
    private static final Logger log = LoggerFactory.getLogger(HandlerMapper.class);
    private static final Map<String, Controller> controllers = new HashMap<>(){{
        put("GET/users/create", new UserCreateController());
    }};
    private static final Controller staticFileController = new StaticFileController();

    public static void map(Request request, Response response) throws IOException {
        controllers.getOrDefault(request.getMethod()+request.getUri(), staticFileController).start(request, response);
    }
}
