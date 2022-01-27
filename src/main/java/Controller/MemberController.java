package Controller;

import webserver.HttpResponse;
import webserver.annotations.GetMapping;
import webserver.annotations.PostMapping;
import webserver.enums.HttpStatus;

public class MemberController {
    @GetMapping("/index.html")
    public HttpResponse index() {
        return HttpResponse.httpStatus(HttpStatus.OK).setView("/index.html");
    }

    @GetMapping("/user/form.html")
    public HttpResponse form() {
        return HttpResponse.httpStatus(HttpStatus.OK).setView("/user/form.html");
    }

    @PostMapping("/user/create")
    public HttpResponse create() {
        System.out.println("yoyo");
        return HttpResponse.httpStatus(HttpStatus.FOUND).redirect("/index.html");
    }
}
