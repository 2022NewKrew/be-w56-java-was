package controller.user;

import http.request.Request;
import model.ModelAndView;

public class LoginController implements UserController{
    @Override
    public ModelAndView proceed(Request request) {
        boolean login = userService.login(request.getRequestBody());
        ModelAndView mv = new ModelAndView("redirect:/");
        mv.addObject("login", login);
        if(login){
            mv.addObject("user", userService.searchUserById(request.getRequestBody()));
        }
        return mv;
    }
}
