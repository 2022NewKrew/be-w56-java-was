package controller;

import lombok.extern.slf4j.Slf4j;
import model.RequestHeader;
import model.ResponseHeaderBuilder;
import model.ResponseHeader;

@Slf4j
public class RequestController {
    private RequestController() {

    }

    public static ResponseHeader controlRequest(RequestHeader requestHeader) {
        String uri = requestHeader.getHeaders("uri");
        String method = requestHeader.getHeaders("method");
        log.info("CONTROL URI: " + uri);
        log.info("CONTROL METHOD: " + method);

        ResponseHeaderBuilder responseHeaderBuilder = new ResponseHeaderBuilder(requestHeader);

        if (uri.equals("/user/create") && method.equals("POST")) {
            return responseHeaderBuilder.postUserCreate();
        }

        if (uri.equals("/user/login") && method.equals("POST")) {
            return responseHeaderBuilder.postUserLogin();
        }

        return responseHeaderBuilder.normalRequest(uri);
    }
}
