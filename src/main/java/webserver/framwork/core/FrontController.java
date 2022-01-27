package webserver.framwork.core;

import webserver.framwork.http.request.HttpRequest;
import webserver.framwork.http.response.HttpResponse;
import webserver.framwork.http.response.HttpStatus;

import java.lang.reflect.Method;

public class FrontController {

    private final HandlerMapping handlerMapping = HandlerMapping.getInstance();
    private final ViewResolver viewResolver = ViewResolver.getInstance();

    private static FrontController instance;

    private FrontController() {
    }

    public static FrontController getInstance() {
        if (instance == null) {
            instance = new FrontController();
        }
        return instance;
    }

    public void process(HttpRequest request, HttpResponse response) {
        try {
            Method method = handlerMapping.getHandlerMethod(request.getMethod(), request.getUrl());
            Object instance = handlerMapping.getHandlerInstance(request.getMethod(), request.getUrl());
            if (method == null || instance == null) {
                response.setStatus(HttpStatus.BadRequest);
                return;
            }
            Model model = new Model();
            Object[] params = new Object[3];
            params[0] = request;
            params[1] = response;
            params[2] = model;
            String viewName = (String) method.invoke(instance, params);

            viewResolver.resolve(viewName, model, request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
