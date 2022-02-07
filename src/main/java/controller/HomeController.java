package controller;

import model.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;

import static controller.RequestPathMapper.response302Header;

public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    protected static void homePath(RequestHeader requestHeader, DataOutputStream dos){
        if(requestHeader.isCookie()){
            log.info("Log-in User");
            response302Header(requestHeader.getContentType(), "/user/list.html", dos);
            return;
        }
        log.info("Redirect to Log-in page");
        response302Header(requestHeader.getContentType(), "/user/login.html", dos);
    }
}
