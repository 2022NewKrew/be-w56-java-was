package webserver;

import DTO.RequestHeader;
import DTO.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.Controller;


public class DispatcherServlet {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    static {
        HandlerMapper.initController();
    }

    public static void handleRequest(RequestHeader requestHeader, ResponseHeader responseHeader) {
        //requestMethod(requestHeader.checkMethod(), requestHeader);
        try {
            Controller controller = HandlerMapper.requestMapping(requestHeader);
            controller.getResponse(requestHeader, responseHeader);
        } catch (NullPointerException e) {

        }

    }
}
