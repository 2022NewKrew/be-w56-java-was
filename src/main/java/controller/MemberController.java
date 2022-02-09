package controller;

import dto.UserCreateDto;
import dto.UserItemDto;
import dto.UserSignInDto;
import exception.BusinessException;
import exception.EntityNotFoundException;
import org.h2.engine.Mode;
import service.MemberService;
import webserver.model.ModelAndView;
import webserver.model.http.Cookie;
import webserver.model.http.HttpResponse;
import webserver.annotations.Autowired;
import webserver.annotations.Component;
import webserver.annotations.GetMapping;
import webserver.annotations.PostMapping;
import webserver.enums.HttpStatus;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.List;

@Component
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping(value = {"/index.html", "/"})
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/index.html");
        return modelAndView;
    }

    @GetMapping("/user/login.html")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/user/login.html");
        return modelAndView;
    }

    @GetMapping("/user/form.html")
    public ModelAndView form() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/user/form.html");
        return modelAndView;
    }

    @PostMapping("/user/create")
    public ModelAndView create(UserCreateDto userCreateDto) {
        memberService.create(userCreateDto);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/index.html");
        return modelAndView;
    }

    @PostMapping("/user/login")
    public ModelAndView signIn(UserSignInDto userSignInDto, HttpResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            memberService.signIn(userSignInDto);
        } catch (EntityNotFoundException e) {
            modelAndView.setViewName("redirect:/user/login_failed.html");
            return modelAndView;
        }

        response.setCookie(new Cookie()
                .add("login", "true")
                .add("Path", "/"));

        modelAndView.setViewName("redirect:/index.html");
        return modelAndView;
    }

    @GetMapping("/user/login_failed.html")
    public ModelAndView loginFailed() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/user/login_failed.html");
        return modelAndView;
    }

    @GetMapping("/user/list.html")
    public ModelAndView getUserList() {
        List<UserItemDto> userItemDtoList = memberService.getList();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setModel("userList", userItemDtoList);
        modelAndView.setViewName("/user/list.html");
        return modelAndView;
    }
}
