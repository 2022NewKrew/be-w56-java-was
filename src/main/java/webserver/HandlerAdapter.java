package webserver;

import DTO.ModelAndView;
import DTO.RequestHeader;
import DTO.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.Controller;

public class HandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(HandlerAdapter.class);

    public static ModelAndView request(Controller controller, RequestHeader requestHeader, ResponseHeader responseHeader) throws NullPointerException{
        if (controller == null) {
            throw new NullPointerException("No controller exists !");
        }
        return controller.getResponse(requestHeader, responseHeader);

    }

}
