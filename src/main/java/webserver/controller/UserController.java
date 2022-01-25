package webserver.controller;

import webserver.domain.User;
import webserver.request.HttpRequest;

import java.io.DataOutputStream;
import java.io.IOException;

public class UserController implements Controller{
    private static final String BASE_URL = "/user";
    private static final String JOIN_URL = "/user/create";

    @Override
    public boolean supports(String url) {
        return url.startsWith(BASE_URL);
    }

    @Override
    public void handle(HttpRequest httpRequest, DataOutputStream dos) throws IOException {
        if(httpRequest.getUrl().startsWith(JOIN_URL)){
            handleJoin(httpRequest, dos);
        }
    }

    private void handleJoin(HttpRequest httpRequest, DataOutputStream dos) throws IOException {
        User user = createUser(httpRequest);

        byte[] body = "가입이 완료되었습니다.".getBytes();
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private User createUser(HttpRequest httpRequest){
        return new User(httpRequest.getQueryParam("userId")
                , httpRequest.getQueryParam("password")
                , httpRequest.getQueryParam("name")
                , httpRequest.getQueryParam("email"));
    }
}
