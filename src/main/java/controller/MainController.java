package controller;

import dto.MemoDto;
import dto.UserDto;
import exception.UserException;
import lombok.extern.slf4j.Slf4j;
import service.MemoService;
import service.UserService;
import webserver.annotation.RequestMapping;
import webserver.annotation.RequestMethod;
import webserver.model.Model;
import webserver.model.WebHttpRequest;
import webserver.model.WebHttpResponse;

@Slf4j
public class MainController {
    private static final MainController INSTANCE = new MainController();

    public static MainController getInstance() {
        return INSTANCE;
    }

    private MainController() {
        userService = UserService.getInstance();
        memoService = MemoService.getInstance();
    }

    private final UserService userService;
    private final MemoService memoService;

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String index(WebHttpRequest httpRequest, Model model) {
        log.info("MainController - index()");
        model.addAttribute("memos", memoService.getAllMemo());
        return "/index.html";
    }

    @RequestMapping(value = {"/index", "/index.html"}, method = RequestMethod.GET)
    public String indexRedirect() {
        return "redirect:/";
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public String login(WebHttpResponse httpResponse, UserDto dto) {
        log.info("MainController - login() " + dto);
        httpResponse.addCookie("logined", "false");
        try {
            userService.login(dto);
            httpResponse.addCookie("logined", "true");
            httpResponse.addCookie("userId", dto.getUserId());
            return "redirect:/index.html";
        } catch (UserException e) {
            return "/user/login_failed.html";
        }
    }

    //TODO /user로 변경하기
    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String createUser(UserDto dto) {
        log.info("MainController - createUser() " + dto);
        try {
            userService.register(dto);
        } catch (UserException e) {
            return "/user/form.html";
        }
        return "redirect:/index.html";
    }

    @RequestMapping(value = {"/user/list", "/user/list.html"}, method = RequestMethod.GET)
    public String userList(WebHttpRequest httpRequest, Model model) {
        log.info("MainController - userList()");
        if (!checkLogin(httpRequest)) {
            return "redirect:/index.html";
        }
        model.addAttribute("users", userService.getAllUser());
        return "/user/list.html";
    }

    @RequestMapping(value = {"/memo", "/memo/form", "/memo/form.html"}, method = RequestMethod.GET)
    public String memoForm(WebHttpRequest httpRequest, Model model) {
        log.info("MainController - memoList()");
        if (!checkLogin(httpRequest)) {
            return "redirect:/index.html";
        }
        String userId = httpRequest.getCookie("userId");
        UserDto userDto = userService.getUserById(userId);
        model.addAttribute("userId", userDto.getUserId());
        model.addAttribute("writerName", userDto.getName());
        return "/memo/form.html";
    }

    @RequestMapping(value = "/memo", method = RequestMethod.POST)
    public String createMemo(MemoDto dto) {
        log.info("MainController - createMemo() " + dto);
        try {
            memoService.register(dto);
        } catch (UserException e) {
            return "/memo/form.html";
        }
        return "redirect:/index.html";
    }

    private boolean checkLogin(WebHttpRequest httpRequest) {
        return httpRequest.getCookie("logined") != null && httpRequest.getCookie("logined").equals("true");
    }
}
