package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.request.HttpRequest;
import util.request.MethodType;
import util.response.HttpResponse;
import util.response.HttpResponseStatus;
import webserver.domain.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class UserController implements Controller<String>{
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private static final String BASE_URL = "/user";
    private static final String JOIN_URL = BASE_URL.concat("/create");

    @Override
    public boolean supports(MethodType methodType, String url) {
        return url.startsWith(JOIN_URL) && methodType == MethodType.POST;
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
        Map<String, String > bodyParams
                = Arrays.stream(httpRequest.getBody().split("&"))
                .map(parameter -> {
                    String[] split = parameter.split("=");
                    return Map.entry(split[0], split[1]);})
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new User(bodyParams.get("userId")
                , bodyParams.get("password")
                , bodyParams.get("name")
                , bodyParams.get("email"));
    }
}
