package controller;

import lombok.extern.slf4j.Slf4j;
import model.RequestHeader;
import model.ResponseHeader;
import model.SignUpService;
import model.builder.NormalRequestBuilder;
import model.builder.PostUserCreateBuilder;
import model.builder.PostUserLoginBuilder;

@Slf4j
public class RequestController {
    private RequestController() {

    }

    public static ResponseHeader controlRequest(RequestHeader requestHeader) throws Exception {
        String uri = requestHeader.getHeader("uri");
        String method = requestHeader.getHeader("method");
        log.info("CONTROL URI: " + uri);
        log.info("CONTROL METHOD: " + method);

        if (uri.equals("/user/create") && method.equals("POST")) {
            SignUpService.signup(requestHeader);
            return new PostUserCreateBuilder().build(requestHeader);
        }

        if (uri.equals("/user/login") && method.equals("POST")) {
            return new PostUserLoginBuilder().build(requestHeader);
        }

        return new NormalRequestBuilder().build(requestHeader);
    }
}
