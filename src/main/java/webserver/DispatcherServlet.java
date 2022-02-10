package webserver;

import DTO.ModelAndView;
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

    public static ModelAndView handleRequest(RequestHeader requestHeader, ResponseHeader responseHeader) throws NullPointerException{

        try {
            Controller controller = HandlerMapper.requestMapping(requestHeader);
            log.error(" handle request : {}", controller);
            ModelAndView modelAndView = HandlerAdapter.request(controller, requestHeader, responseHeader);

            return modelAndView;

        } catch (NullPointerException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
