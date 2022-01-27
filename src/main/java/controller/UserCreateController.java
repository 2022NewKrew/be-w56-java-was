package controller;

import http.request.HttpRequest;
import http.response.HttpResponse;
import http.HttpStatus;
import service.UserService;

public class UserCreateController extends AbstractController {

    private static final String PATH = "/user/create";

    private static UserCreateController instance;

    private UserCreateController() {
    }

    public static UserCreateController getInstance() {
        if (instance == null) {
            instance = new UserCreateController();
        }
        return instance;
    }


    @Override
    protected HttpResponse doGet(HttpRequest request) {
        return HttpResponse.error(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected HttpResponse doPost(HttpRequest request) {
        UserService.register(request.getBody().getParams());
        return HttpResponse.redirect("/index.html");
    }

    @Override
    public boolean match(String path) {
        return PATH.equals(path);
    }
}
