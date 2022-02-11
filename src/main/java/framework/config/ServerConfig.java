package framework.config;

import application.controller.Controller;
import framework.HandlerMapping;
import framework.ViewResolver;
import framework.handler.Handler;
import framework.handler.HandlerMethod;
import framework.handler.RequestMappingHandler;
import framework.handler.ResourceRequestHandler;
import framework.modelAndView.view.RedirectView;
import framework.modelAndView.view.TemplateView;
import framework.annotation.RequestMapping;
import framework.modelAndView.view.StaticView;
import framework.modelAndView.View;

import java.lang.reflect.Method;
import java.util.*;

public class ServerConfig {

    public static List<Controller> controllers = new ArrayList<>();
    public static List<Handler> handlers = new ArrayList<>();
    public static List<HandlerMethod> handlerMethods = new ArrayList<>();
    public static HandlerMapping handlerMapping;
    public static Map<String, View> viewMap = new LinkedHashMap<>();
    public static ViewResolver viewResolver;

    // Controller 등록 - HandlerMethod 수집
    static {
        controllers.add(Beans.userController);
        controllers.add(Beans.memoController);
        initializeHandlerMethod();
    }

    // Handler 등록
    static {
        // 수집된 handlerMethods 주입
        handlers.add(new RequestMappingHandler(handlerMethods));
        handlers.add(new ResourceRequestHandler());
        initializeHandlerMapping();
    }

    static {
        viewMap.put("redirect:/.*", new RedirectView());
        viewMap.put("/.+\\.html", new StaticView());
        viewMap.put("/.+", new TemplateView());
        initializeViewResolver();
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

    private static void initializeViewResolver() {
        viewResolver = new ViewResolver(viewMap);
    }
}
