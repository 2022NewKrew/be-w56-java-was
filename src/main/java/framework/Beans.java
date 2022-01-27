package framework;

import framework.FrontController;
import framework.HandlerMapping;
import framework.ViewResolver;
import mvc.controller.Controller;
import mvc.controller.ResourceController;
import mvc.controller.UserController;
import mvc.service.UserService;

import java.util.Arrays;
import java.util.List;

/**
 * 의존성 주입을 위한 Beans 클래스, 의존 관계의 역순으로 인스턴스를 생성
 */
public class Beans {
    // controller
    public static final UserService userService = new UserService();
    // handlerMapping
    public static final Controller resourceController = new ResourceController();
    public static final List<Controller> controllerList = Arrays.asList(
            new UserController(userService)
    );
    // frontController
    public static final HandlerMapping handlerMapping = new HandlerMapping(resourceController, controllerList);
    public static final ViewResolver viewResolver = new ViewResolver();
    public static final FrontController frontController = new FrontController(handlerMapping, viewResolver);
}
