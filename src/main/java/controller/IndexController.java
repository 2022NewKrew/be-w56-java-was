package controller;

import http.HttpMethod;
import model.ModelAndView;
import http.request.Request;
import util.RequestMapping;

public class IndexController implements Controller{

    private static final IndexController indexController = new IndexController();

    private IndexController(){}

    public static IndexController getInstance(){
        return indexController;
    }

    @RequestMapping(method = HttpMethod.GET, url = "/")
    public ModelAndView proceed(Request request) {
        return new ModelAndView("/");
    }
}
