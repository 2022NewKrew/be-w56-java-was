package webserver;

import DTO.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestPathUtils;
import webserver.controller.Controller;

import java.util.Map;

import static webserver.HandlerMapper.requestMapping;

public class DispatcherServlet {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private static final boolean GET = true;
    private static final boolean POST = false;

    static {
        HandlerMapper.initController();
    }

    public static void handleRequest(RequestHeader requestHeader){
        //requestMethod(requestHeader.checkMethod(), requestHeader);
        Controller controller = HandlerMapper.requestMapping(requestHeader);

    }
//
//    private static void requestPost(RequestHeader requestHeader){
//        Map<String, String> requestParam = requestHeader.getBody();
//        String requestUrlOnly = requestHeader.getRequestUrl();
//
//        requestMapping(requestUrlOnly);
//    }
}
