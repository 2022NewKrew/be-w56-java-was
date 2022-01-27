package http;


import controller.Controller;
import controller.IndexController;
import controller.UserController;
import http.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class HandlerMapper {
    private static final Logger log = LoggerFactory.getLogger(HandlerMapper.class);

    private final Map<String, Controller> controllerMap = new ConcurrentHashMap<>();

    private static final HandlerMapper handlerMapper = new HandlerMapper();

    private HandlerMapper(){
        controllerMap.put("/", IndexController.getInstance());
        controllerMap.put("/users", UserController.getInstance());
    }

    public static HandlerMapper getInstance(){
        return handlerMapper;
    }

    public Controller get(Request request) {
        log.info("[HandlerMapper] : " + request.isStatic() + " " + request.getUrl() + " |||||| " + request.getFirstUrl());
        return request.isStatic()? controllerMap.get(request.getUrl()) : controllerMap.get(request.getFirstUrl());
    }
}
