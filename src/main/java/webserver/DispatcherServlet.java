package webserver;

import DTO.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestPathUtils;

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
        requestMethod(requestHeader.checkMethod(), requestHeader);
    }

    private static void requestMethod(boolean getOrPost, RequestHeader headerDTO){
        // Do GET Method
        if(getOrPost == GET){
            requestGet(headerDTO);
            return;
        }

        // Do POST Method
        requestPost(headerDTO);
    }

    private static void requestGet(RequestHeader headerDTO){
        String requestUrl = headerDTO.getRequestUrl();
//        if (RequestPathUtils.containsParam(requestUrl)){
//            log.info("Request Url Contains Parameters");
//            requestMapping(requestUrlOnly, requestParam);
//        }
    }


    private static void requestPost(RequestHeader requestHeader){
        Map<String, String> requestParam = requestHeader.getBody();
        String requestUrlOnly = requestHeader.getRequestUrl();
        log.info("dispatcherServlet-- request POST started !");
        requestMapping(requestUrlOnly);
    }
}
