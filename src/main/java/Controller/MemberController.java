package Controller;

import db.DataBase;
import dto.UserCreateDto;
import model.User;
import webserver.HttpResponse;
import webserver.annotations.GetMapping;
import webserver.annotations.PostMapping;
import webserver.enums.HttpStatus;

public class MemberController {
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
        User user = User.of(userCreateDto);
        DataBase.addUser(user);
        return HttpResponse.httpStatus(HttpStatus.FOUND).redirect("/index.html");
    }
}
