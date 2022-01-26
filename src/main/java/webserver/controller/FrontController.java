package webserver.controller;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import webserver.http.*;
import webserver.view.ViewResolver;


public class FrontController implements HttpController{
    private static final Table<HttpMethod, String, HttpController> controllerMap = HashBasedTable.create();
    private static FrontController frontController;
    private FrontController(){
        controllerMap.put(HttpMethod.POST, "/user/create", new RegisterController());
        controllerMap.put(HttpMethod.POST, "/user/login", new LoginController());
    }

    public static FrontController getInstance(){
        if(frontController == null){
            frontController = new FrontController();
        }
        return frontController;
    }

    @Override
    public HttpResponse process(HttpRequest request){
        try{
            ViewResolver resolver = ViewResolver.getInstance();

            if(controllerMap.contains(request.getMethod(), request.getUrl())){
                HttpController controller = controllerMap.get(request.getMethod(), request.getUrl());
                return resolver.findView(controller.process(request));
            }

            return resolver.findView(new HttpResponse(HttpStatus.OK, request.getUrl()));
        } catch(IllegalArgumentException e){
            return new HttpResponse(HttpStatus.BAD_REQUEST, HttpConst.ERROR_PAGE);
        }
    }
}
