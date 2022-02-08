package controller.user;

import controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import service.auth.UserListService;
import webserver.http.Cookie;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.model.ModelAndView;

@Slf4j
public class UserListController implements BaseController {

    private final UserListService userListService = new UserListService();

    @Override
    public ModelAndView process(HttpRequest request, HttpResponse response) {
        if (!isLogin(request)) {
            log.warn("로그인이 필요한 서비스 입니다.");
            return new ModelAndView("/user/login.html", HttpStatus.FOUND);
        }

        ModelAndView modelAndView = new ModelAndView("/user/list.html");
        modelAndView.addAttribute("users", userListService.getAllUsers());
        return modelAndView;
    }

    private boolean isLogin(HttpRequest request) {
        Cookie cookie = request.getCookieByName("logined");
        return !ObjectUtils.isEmpty(cookie) && cookie.equalsValue("true");
    }

}
