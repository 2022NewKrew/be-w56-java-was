package framework;

import framework.util.ReflectionManager;
import framework.util.TemplateParser;
import mvc.controller.Controller;
import mvc.controller.ResourceController;
import mvc.controller.UserController;
import mvc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import framework.webserver.RequestHandler;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * 의존 관계의 역순으로 인스턴스를 생성
 */
public class Beans {
    public static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    public static final TemplateParser templateParser = new TemplateParser();
    // controller
    public static final UserService userService = new UserService();
    // handlerMapping
    public static final Set<Controller> controllerList = Set.of(new ResourceController(), new UserController(log, userService));
    public static final Map<Method, Object> methodObjectMap = ReflectionManager.getMethodObjectMap(controllerList);
    // frontController
    public static final HandlerMapping handlerMapping = new HandlerMapping(methodObjectMap);
    public static final View view = new View(templateParser);
    public static final ViewResolver viewResolver = new ViewResolver(view);
    public static final FrontController frontController = new FrontController(handlerMapping, viewResolver);
}
