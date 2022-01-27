package controllers;

import model.Request;
import model.Response;
import service.UserService;

public class UserController extends Controller {

    private final String PREFIX = "/user";
    private String url;

    @Override
    public void getMethod(Request request, Response response) {

    }

    @Override
    public void postMethod(Request request, Response response) {
        this.url = request.getRequestHeader().getRequestLine().getUrl();

        if (url.startsWith(PREFIX + "/create")) {
            joinController(request, response);
        } else if (url.startsWith(PREFIX + "/login")) {
            loginController(request, response);
        }
    }

    private void loginController(Request request, Response response) {
        Boolean loginResult = UserService.loginService(request);
        if (loginResult) {
            response.build302ResponseWithCookie("/index.html", "true");
        }
        response.build302ResponseWithCookie("/user/login_failed.html", "false");
    }

    private void joinController(Request request, Response response) {
        UserService.joinService(request);

        response.build302Response();
    }
}
