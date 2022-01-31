package controller;

import exception.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import model.RequestHeader;
import model.ResponseHeader;
import model.SignUpService;
import model.builder.NormalRequestBuilder;
import model.builder.PostUserCreateBuilder;
import model.builder.PostUserLoginBuilder;
import model.builder.ResponseBuilder;

@Slf4j
public class RequestController {
    private RequestController() {

    }

    public static ResponseHeader controlRequest(RequestHeader requestHeader) throws Exception {
        String uri = requestHeader.getHeader("uri");
        String method = requestHeader.getHeader("method");
        log.info("CONTROL URI: " + uri);
        log.info("CONTROL METHOD: " + method);

        ResponseBuilder responseBuilder = new NormalRequestBuilder();

        if (uri.equals("/user/create") && method.equals("POST")) {
            SignUpService.signup(requestHeader);
            responseBuilder = new PostUserCreateBuilder();
        }

        if (uri.equals("/user/login") && method.equals("POST")) {
            responseBuilder = new PostUserLoginBuilder();
        }
        return responseBuilder.build(requestHeader);
    }
}
