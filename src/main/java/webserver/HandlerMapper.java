package webserver;

import DTO.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.Controller;
import webserver.controller.MemberController;
import webserver.controller.StaticController;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapper {
    private static final Logger log = LoggerFactory.getLogger(HandlerMapper.class);
    final static Map<String, Controller> controllerList = new HashMap<>();
    private static final Controller staticController = new StaticController();

    final private static String CREATE_USER = "/user/create";

    static void initController(){
        Controller memberController = new MemberController();
        memberController.map(controllerList);
    }

    public static Controller requestMapping(RequestHeader requestHeader) throws NullPointerException{
        String requestUrl = requestHeader.getRequestUrl();

        log.info("[HandlerMapper] request url: {}",requestUrl);
        log.info("[HandlerMapper] existing url list: {}",controllerList.keySet());

        if (! controllerList.containsKey(requestUrl)) {

            return staticController;

            // todo : static file 도 없는 경우!
            //throw new NullPointerException("No controller exists! - url name:"+ requestUrl);
        }

        return controllerList.get(requestUrl);


    }
}
