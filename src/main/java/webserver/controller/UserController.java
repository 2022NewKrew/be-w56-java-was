package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.request.HttpRequest;
import util.response.HttpResponse;
import util.response.HttpResponseDataType;
import util.response.HttpResponseStatus;
import webserver.domain.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class UserController implements Controller<String>{
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private static final String BASE_URL = "/user";
    private static final String JOIN_URL = "/user/create";

    @Override
    public boolean supports(String url) {
        return url.startsWith(BASE_URL);
    }

    @Override
    public HttpResponse<String> handle(HttpRequest httpRequest, DataOutputStream dos) throws IOException {
        if(httpRequest.getUrl().startsWith(JOIN_URL)){
            return handleJoin(httpRequest, dos);
        }

        return HttpResponse.<String>builder()
                .status(HttpResponseStatus.NOT_FOUND)
                .build();
    }

    private HttpResponse<String> handleJoin(HttpRequest httpRequest, DataOutputStream dos) throws IOException {
        User user = createUser(httpRequest);
        log.info("created user {}", user);

        return HttpResponse.<String>builder()
                .status(HttpResponseStatus.REDIRECT)
                .headers(Map.of("Location", "/"))
                .build();
    }

    private User createUser(HttpRequest httpRequest){
        return new User(httpRequest.getQueryParam("userId")
                , httpRequest.getQueryParam("password")
                , httpRequest.getQueryParam("name")
                , httpRequest.getQueryParam("email"));
    }
}
