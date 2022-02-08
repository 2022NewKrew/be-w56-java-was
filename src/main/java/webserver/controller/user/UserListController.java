package webserver.controller.user;

import lombok.RequiredArgsConstructor;
import util.request.HttpRequest;
import util.request.MethodType;
import util.response.*;
import webserver.controller.Controller;
import webserver.domain.entity.User;
import webserver.domain.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
public class UserListController implements Controller {
    private final UserRepository userRepository;

    @Override
    public boolean supports(HttpRequest httpRequest) {
        return httpRequest.getMethod() == MethodType.GET
                && httpRequest.getUrl().equals("/users");
    }

    @Override
    public HttpResponse doHandle(HttpRequest httpRequest) {
        List<User> users = userRepository.getUsers();
        ModelAndView mav = new ModelAndView("/user/list.html");
        mav.addAttribute("users", users);

        ResponseHeaders headers = ResponseHeaders.builder()
                .contentType(ContentType.HTML)
                .build();

        return HttpResponse.builder()
                .status(HttpStatus.SUCCESS)
                .modelAndView(mav)
                .headers(headers)
                .build();
    }
}
