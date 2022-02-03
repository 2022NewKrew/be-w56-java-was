package framework;

import com.google.common.collect.Sets;
import framework.util.ReflectionManager;
import mvc.controller.Controller;
import mvc.controller.ResourceController;
import mvc.controller.UserController;
import mvc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * 의존 관계의 역순으로 인스턴스를 생성
 */
public class Beans {
    public static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    // controller
    public static final UserService userService = new UserService();
    // handlerMapping
    public static final Set<Controller> controllerList = Sets.newConcurrentHashSet(
            Arrays.asList(
                    new ResourceController(),
                    new UserController(userService)
            )
    );
    public static final Map<Method, Object> methodObjectMap = ReflectionManager.getMethodObjectMap(controllerList);
    // frontController
    public static final HandlerMapping handlerMapping = new HandlerMapping(methodObjectMap);
    public static final ViewResolver viewResolver = new ViewResolver();
    public static final FrontController frontController = new FrontController(handlerMapping, viewResolver);
}
