package webserver.controller.user;

import dto.UserCreateDto;
import webserver.Request;
import webserver.Response;

public class UserPostCreateController implements UserController {
    @Override
    public String control(Request request) {
        System.out.println(request.getHeaderAttributes().toString());
        return "redirect:/index.html";
    }
}
