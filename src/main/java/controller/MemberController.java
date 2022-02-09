package controller;

import dto.UserCreateDto;
import dto.UserSignInDto;
import exception.BusinessException;
import service.MemberService;
import webserver.http.Cookie;
import webserver.http.HttpResponse;
import webserver.annotations.Autowired;
import webserver.annotations.Component;
import webserver.annotations.GetMapping;
import webserver.annotations.PostMapping;
import webserver.http.enums.HttpStatus;

@Component
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping(value = {"/index.html", "/"})
    public HttpResponse index() {
        return HttpResponse.httpStatus(HttpStatus.OK).setView("/index.html");
    }

    @GetMapping("/user/login.html")
    public HttpResponse login() {
        return HttpResponse.httpStatus(HttpStatus.OK).setView("/user/login.html");
    }

    @GetMapping("/user/form.html")
    public HttpResponse form() {
        return HttpResponse.httpStatus(HttpStatus.OK).setView("/user/form.html");
    }

    @PostMapping("/user/create")
    public HttpResponse create(UserCreateDto userCreateDto) {
        memberService.create(userCreateDto);
        System.out.println(userCreateDto.getUserId());
        System.out.println(userCreateDto.getPassword());
        return HttpResponse.httpStatus(HttpStatus.FOUND).redirect("/index.html");
    }

    @PostMapping("/user/login")
    public HttpResponse signIn(UserSignInDto userSignInDto) {
        try {
            memberService.signIn(userSignInDto);
        } catch (BusinessException e) {
            return HttpResponse.httpStatus(HttpStatus.FOUND).redirect("/user/login_failed.html");
        }

        Cookie cookie = new Cookie()
                .add("login", "true")
                .add("Path", "/");

        return HttpResponse.httpStatus(HttpStatus.FOUND).setCookie(cookie).redirect("/index.html");
    }

    @GetMapping("/user/login_failed.html")
    public HttpResponse loginFailed() {
        return HttpResponse.httpStatus(HttpStatus.OK).setView("/user/login_failed.html");
    }
}
