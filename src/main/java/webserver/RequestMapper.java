package webserver;

import lombok.extern.slf4j.Slf4j;
import webserver.controller.Controller;
import webserver.controller.StaticController;
import webserver.controller.UserController;
import webserver.web.request.Request;

import java.util.*;

@Slf4j
public class RequestMapper {

    private final static List<Controller> controllers = new ArrayList<>();
    private static RequestMapper requestMapper = new RequestMapper();

    private RequestMapper() {
        controllers.add(new StaticController());
        controllers.add(new UserController());

        log.debug("{} created : {}", this.getClass(), this);
    }

    public static RequestMapper getInstance() {
        return requestMapper;
    }

    public Optional<Controller> mapping(Request request) {
        return controllers.stream().filter(controller -> controller.isSupply(request)).findAny();
    }
}
