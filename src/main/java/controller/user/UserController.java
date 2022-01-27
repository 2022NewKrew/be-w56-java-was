package controller.user;

import controller.Controller;
import http.request.Request;
import model.ModelAndView;
import service.UserService;

public interface UserController extends Controller {
    UserService userService = UserService.getInstance();
    ModelAndView proceed(Request request);
}
