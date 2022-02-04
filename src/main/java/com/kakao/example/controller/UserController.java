package com.kakao.example.controller;

import com.kakao.example.application.service.UserService;
import com.kakao.example.model.domain.User;
import com.kakao.example.util.exception.UserNotFoundException;
import framework.util.HttpSession;
import framework.util.annotation.Autowired;
import framework.util.annotation.Component;
import framework.util.annotation.RequestMapping;
import framework.view.ModelView;
import framework.webserver.HttpRequestHandler;
import framework.webserver.HttpResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static framework.util.annotation.Component.ComponentType.CONTROLLER;

@Component(type = CONTROLLER)
@RequestMapping("/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

        // 미리 관리자 계정을 추가
        userService.addUser(User.builder()
                .userId("admin")
                .password("admin")
                .name("관리자")
                .email("admin@kakao.com")
                .build());
    }

    // GET 방식으로 회원가입, 2단계 미션 수행을 위해 존재
    @RequestMapping(value = "/create", requestMethod = "GET")
    public String userRegisterByGet(HttpRequestHandler request) {
        LOGGER.debug("Register User by GET method");

        userService.addUser(User.builder()
                .userId(request.getAttribute("userId"))
                .password(request.getAttribute("password"))
                .name(request.getAttribute("name"))
                .email(request.getAttribute("email"))
                .build());

        return "redirect:/";
    }

    // 회원가입
    @RequestMapping(value = "/create", requestMethod = "POST")
    public String userRegisterByPost(HttpRequestHandler request) {
        LOGGER.debug("Register User by POST method");

        userService.addUser(User.builder()
                .userId(request.getAttribute("userId"))
                .password(request.getAttribute("password"))
                .name(request.getAttribute("name"))
                .email(request.getAttribute("email"))
                .build());

        return "redirect:/";
    }

    // 로그인
    @RequestMapping(value = "/login", requestMethod = "POST")
    public String userLogin(HttpRequestHandler request) {
        LOGGER.debug("Login User");
        String userId = request.getAttribute("userId");
        String password = request.getAttribute("password");

        HttpSession session = request.getSession();

        try {
            userService.findUserByLoginInfo(userId, password);
        } catch (UserNotFoundException e) {
            return "redirect:/user/login_failed";
        }

        // 로그인한 아이디를 Session에 저장
        session.setAttribute("USER_ID", userId);
        return "redirect:/";
    }

    // 로그아웃
    @RequestMapping(value = "/logout", requestMethod = "GET")
    public String userLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // 회원 목록
    @RequestMapping(value = "/list", requestMethod = "GET")
    public String userList(HttpSession session, ModelView modelView) {
        LOGGER.debug("Get User List");

        // 이미 로그인한 상태가 아니라면 로그인 화면으로 이동
        if (!session.contains("USER_ID")) {
            return "user/login";
        }

        modelView.setAttribute("users", userService.findAll());
        return "user/list";
    }
}
