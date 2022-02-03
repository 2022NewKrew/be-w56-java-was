package webserver.framwork.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.framwork.RequestHandler;
import webserver.framwork.core.view.View;
import webserver.framwork.http.request.HttpRequest;
import webserver.framwork.http.response.HttpResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FrontController {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final FrontController instance = new FrontController();

    private final HandlerMapping handlerMapping = HandlerMapping.getInstance();
    private final ViewResolver viewResolver = ViewResolver.getInstance();
    private final ResourceHandler resourceHandler = ResourceHandler.getInstance();

    private FrontController() {
    }

    public static FrontController getInstance() {
        return instance;
    }

    public void process(HttpRequest request, HttpResponse response) {

        Model model = new Model();
        String viewName = findControllerAndInvoke(request, response, model);

        if (viewName == null) {
            resourceHandler.forward(request, response);
            return;
        }

        View view = viewResolver.resolve(viewName, model);
        view.render(response);
    }

    public String findControllerAndInvoke(HttpRequest request, HttpResponse response, Model model) {
        try {
            Method handlerMethod = handlerMapping.getHandlerMethod(request.getMethod(), request.getUrl());
            if (handlerMethod == null) {
                return null;
            }

            Object handlerInstance = handlerMethod.getDeclaringClass().getMethod("getInstance").invoke(null);
            Object[] params = new Object[]{
                    request,
                    response,
                    model
            };
            return (String) handlerMethod.invoke(handlerInstance, params);

        } catch (NoSuchMethodException e) {
            log.error("Controller에 getInstance() 가 없습니다.");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            log.error("RequestMapping 메소드의 파라미터가 일치하지 않습니다.");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            log.error("메소드에 접근할 수 없습니다.");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            log.error("메소드의 소유자가 아닙니다.");
            e.printStackTrace();
        }

        return null;
    }
}
