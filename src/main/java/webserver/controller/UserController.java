package webserver.controller;

import dto.UserCreateDto;
import service.UserService;
import util.annotation.RequestMapping;
import webserver.view.ModelAndView;
import webserver.Request;
import webserver.Response;

public class UserController {
    private static final UserController instance = new UserController();
    private static final UserService userService = UserService.getInstance();

    private UserController() {}

    public static UserController getInstance() {
        return instance;
    }


    @RequestMapping(value="/users/create", method="GET")
    public ModelAndView createByGet(Request request, Response response) {
        userService.create(UserCreateDto.builder()
                .stringId(request.getParameter("stringId"))
                .password(request.getParameter("password"))
                .name(request.getParameter("name"))
                .email(request.getParameter("email"))
                .build());
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:/index.html");
        return mv;
    }

    @RequestMapping(value="/users", method="POST")
    public ModelAndView createByPost(Request request, Response response) {
        userService.create(UserCreateDto.builder()
                .stringId(request.getParameter("stringId"))
                .password(request.getParameter("password"))
                .name(request.getParameter("name"))
                .email(request.getParameter("email"))
                .build());
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:/index.html");
        return mv;
    }

    @RequestMapping(value="/user/list", method="GET")
    public ModelAndView findAll(Request request, Response response) {
        ModelAndView mv = new ModelAndView();
        mv.addAttribute("users", userService.findAll());
        mv.setViewName("/user/list.html");
        return mv;
    }
}
