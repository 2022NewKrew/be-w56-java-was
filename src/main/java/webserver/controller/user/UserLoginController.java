package webserver.controller.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.request.HttpRequest;
import util.request.MethodType;
import util.response.HttpResponse;
import util.response.HttpStatus;
import webserver.controller.Controller;
import webserver.domain.entity.User;
import webserver.domain.repository.UserRepository;

import java.util.Map;

@RequiredArgsConstructor
public class UserLoginController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
    private static final String LOGIN_URL = "/user/login";

    private final UserRepository userRepository;

    @Override
    public boolean supports(HttpRequest httpRequest) {
        return httpRequest.getUrl().equals(LOGIN_URL)
                && httpRequest.getMethod() == MethodType.POST;
    }

    @Override
    public HttpResponse doHandle(HttpRequest httpRequest){
        if(!supports(httpRequest)){
            throw new IllegalStateException("해당 요청을 지원하지 않습니다.");
        }

        return handleLogin(httpRequest);
    }

    private HttpResponse handleLogin(HttpRequest httpRequest){
        String id = httpRequest.getBodyParams().get("userId");
        String password = httpRequest.getBodyParams().get("password");

        User user =userRepository.getUser(id).orElseThrow();
        if(!user.matchedBy(password)){
            throw new IllegalArgumentException("패스워드가 맞지 않습니다.");
        }

        log.info("user {}가 로그인하였습니다", user);

        return HttpResponse.<String>builder()
                .status(HttpStatus.REDIRECT)
                .headers(Map.of("Set-Cookie", "logined=true",
                        "Location", "/"))
                .build();
    }

}
