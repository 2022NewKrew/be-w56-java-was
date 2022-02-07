package webserver.controller;

import annotation.AnnotationProcessor;
import exception.UnAuthorizedException;
import webserver.http.HttpConst;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.view.ModelAndView;
import webserver.view.View;
import webserver.view.ViewResolver;


public class FrontController {
    private static final FrontController frontController = new FrontController();

    private FrontController() {
    }

    public static FrontController getInstance() {
        return frontController;
    }

    public void process(HttpRequest request, HttpResponse response) {
        try {
            ModelAndView mv = (ModelAndView) AnnotationProcessor.getInstance().requestMappingProcessor(request, response);

            if(mv == null){
                mv = new ModelAndView(request.getUrl());
            }

            View view = ViewResolver.getInstance().resolve(mv);
            view.render(mv.getModel(), request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
