package webserver.controller.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.converter.ConverterService;
import util.request.HttpRequest;
import util.request.MethodType;
import util.response.ContentType;
import util.response.HttpResponse;
import util.response.HttpStatus;
import webserver.controller.Controller;
import webserver.domain.entity.User;
import webserver.domain.repository.UserRepository;

import java.util.Map;

@RequiredArgsConstructor
public class UserJoinController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(UserJoinController.class);
    private static final String JOIN_URL = "/user/create";

    private final UserRepository userRepository;

    @Override
    public boolean supports(HttpRequest httpRequest){
        return httpRequest.getUrl().startsWith(JOIN_URL) && httpRequest.getMethod() == MethodType.POST;
    }

    @Override
    public HttpResponse doHandle(HttpRequest httpRequest){
        if(!supports(httpRequest)){
            throw new IllegalStateException("해당 요청을 지원하지 않습니다.");
        }

        return handleJoin(httpRequest);
    }

    private HttpResponse handleJoin(HttpRequest httpRequest){
        User user = createUser(httpRequest);
        userRepository.saveUser(user);
        log.info("created user {}", user);

        return HttpResponse.builder(HttpStatus.REDIRECT, ContentType.HTML)
                .headers(Map.of("Location", "/"))
                .build();
    }

    private User createUser(HttpRequest httpRequest){
        return ConverterService.convert(httpRequest.getBodyParams(), User.class);
    }
}
