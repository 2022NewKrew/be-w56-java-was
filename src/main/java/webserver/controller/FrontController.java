package webserver.controller;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.view.ViewResolver;


import static webserver.config.ControllerConfig.controllerMap;

public class FrontController implements HttpController{
    private static FrontController frontController;

    public static FrontController getInstance(){
        if(frontController == null){
            frontController = new FrontController();
        }

        return frontController;
    }

    @Override
    public HttpResponse process(HttpRequest request){
        ViewResolver resolver = new ViewResolver();

        if(controllerMap.containsKey(request.getUrl())){
            HttpResponse response = controllerMap.get(request.getUrl()).process(request);
            return resolver.findView(response);
        }

        return resolver.findView(new HttpResponse(HttpStatus.OK, request.getUrl()));
    }
}
