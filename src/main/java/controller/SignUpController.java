package controller;

import http.request.Request;
import model.ModelAndView;
import model.User;

public class SignUpController implements UserController{
    @Override
    public ModelAndView proceed(Request request) {
        User user = userService.createUser(request.getQueries());
        return new ModelAndView("/users/create", "user", user);
    }
}
