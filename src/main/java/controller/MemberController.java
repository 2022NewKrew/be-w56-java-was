package controller;

import db.DataBase;
import dto.UserCreateDto;
import dto.UserSignInDto;
import model.User;
import service.MemberService;
import webserver.HttpResponse;
import webserver.annotations.Autowired;
import webserver.annotations.Component;
import webserver.annotations.GetMapping;
import webserver.annotations.PostMapping;
import webserver.enums.HttpStatus;

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
        return HttpResponse.httpStatus(HttpStatus.FOUND).redirect("/index.html");
    }

    @PostMapping("/user/login")
    public HttpResponse signIn(UserSignInDto userSignInDto) {


        return HttpResponse.httpStatus(HttpStatus.FOUND).redirect("/user/login_failed.html");
    }
}
