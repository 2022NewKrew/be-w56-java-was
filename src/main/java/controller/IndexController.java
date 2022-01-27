package controller;

import model.ModelAndView;
import http.request.Request;

public class IndexController implements Controller{
    @Override
    public ModelAndView proceed(Request request) {
        return new ModelAndView("/");
    }
}
