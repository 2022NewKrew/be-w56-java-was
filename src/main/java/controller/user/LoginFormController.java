package controller.user;

import http.request.Request;
import model.ModelAndView;

public class LoginFormController implements UserController{
    @Override
    public ModelAndView proceed(Request request) {
        return new ModelAndView("/users/login");
    }
}
