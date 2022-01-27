package webserver.controller.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.request.HttpRequest;
import util.request.MethodType;
import util.response.HttpResponse;
import util.response.HttpResponseStatus;
import webserver.controller.Controller;
import webserver.domain.entity.User;
import webserver.domain.repository.UserRepository;

import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public class UserJoinController implements Controller<String> {
    private static final Logger log = LoggerFactory.getLogger(UserJoinController.class);
    private static final String JOIN_URL = "/user/create";

    private final UserRepository userRepository;

    @Override
    public boolean supports(HttpRequest httpRequest){
        return httpRequest.getUrl().startsWith(JOIN_URL) && httpRequest.getMethod() == MethodType.POST;
    }

    @Override
    public HttpResponse<String> doHandle(HttpRequest httpRequest){
        if(!supports(httpRequest)){
            throw new IllegalStateException("해당 요청을 지원하지 않습니다.");
        }

        return handleJoin(httpRequest);
    }

    private HttpResponse<String> handleJoin(HttpRequest httpRequest){
        User user = createUser(httpRequest);
        userRepository.saveUser(user);
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
