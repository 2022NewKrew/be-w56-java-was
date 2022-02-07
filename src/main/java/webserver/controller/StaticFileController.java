package webserver.controller;

import webserver.ModelAndView;
import webserver.Request;
import webserver.Response;

import java.io.IOException;

public class StaticFileController{
    private static StaticFileController instance = new StaticFileController();

    private StaticFileController() {}

    public static StaticFileController getInstance() {
        return instance;
    }

    public ModelAndView control(Request request, Response response) throws IOException {
        ModelAndView mv = new ModelAndView();
        mv.setViewName(request.getUri());
        return mv;
    }
}
