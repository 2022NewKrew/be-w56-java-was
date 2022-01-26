package http;

import controller.Controller;
import model.ModelAndView;
import http.request.Request;

public class HandlerAdapter {

    private  static final HandlerAdapter handlerAdapter = new HandlerAdapter();

    private HandlerAdapter(){}

    public static HandlerAdapter getInstance(){
        return handlerAdapter;
    }

    public ModelAndView handleController(Controller controller, Request request){
        return controller.proceed(request);
    }
}
