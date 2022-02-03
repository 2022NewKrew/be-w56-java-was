package framework.config;

import controller.Controller;
import controller.UserController;
import framework.HandlerMapping;
import framework.ViewResolver;
import framework.handler.Handler;
import framework.handler.HandlerMethod;
import framework.handler.RequestMappingHandler;
import framework.handler.ResourceRequestHandler;
import framework.util.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerConfig {

    public static List<Controller> controllers = new ArrayList<>();
    public static List<Handler> handlers = new ArrayList<>();
    public static List<HandlerMethod> handlerMethods = new ArrayList<>();
    public static HandlerMapping handlerMapping;
    public static ViewResolver viewResolver = new ViewResolver();

    // Controller 등록 - HandlerMethod 수집
    static {
        controllers.add(Beans.userController);
        initializeHandlerMethod();
    }

    // Handler 등록
    static {
        // 수집된 handlerMethods 주입
        handlers.add(new RequestMappingHandler(handlerMethods));
        handlers.add(new ResourceRequestHandler());
        initializeHandlerMapping();
    }

    // Controller 에서 Annotation(@RequestMapping) 이 붙은 method 를 handlerMethod 로 등록
    private static void initializeHandlerMethod() {
        for (Controller controller : controllers) {
            Arrays.stream(controller.getClass().getMethods())
                    .forEach(method -> createHandlerMethod(controller, method));
        }
    }

    // Annotation(@RequestMapping) 분석하여 HandlerMethod 생성
    private static void createHandlerMethod(Controller controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null)
            return ;
        handlerMethods.add(new HandlerMethod(controller, method, requestMapping.method(), requestMapping.path()));
    }

    private static void initializeHandlerMapping() {
        handlerMapping = new HandlerMapping();
        handlers.stream().forEach(h -> handlerMapping.addHandler(h));
    }
}
