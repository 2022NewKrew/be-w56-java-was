package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.Controller;
import webserver.controller.MemberController;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapper {
    private static final Logger log = LoggerFactory.getLogger(HandlerMapper.class);
    final static Map<String, Controller> controllerList = new HashMap<>();

    final private static String CREATE_USER = "/user/create";

    static void initController(){
        Controller memberController = new MemberController();
        memberController.map(controllerList);
    }

    public static Controller requestMapping(String requestUrl){
        log.info("[handlerMapper] request url: {}",requestUrl);
        log.info("[handlerMapper] request url: {}",controllerList.keySet());
        return controllerList.get(requestUrl);
    }
}
