package servlet;

import http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.controller.UserController;

import java.lang.reflect.Method;
import java.util.Map;

public class Servlet {
    private static final Logger logger = LoggerFactory.getLogger(Servlet.class);
    private final Object controller;
    private final CustomMethod customMethod;

    private Servlet(Object controller, CustomMethod customMethod) {
        this.controller = controller;
        this.customMethod = customMethod;
    }

    public static Servlet create(Method method) {
        logger.debug("Create ServletContainer method : {}", method);
        return new Servlet(new UserController(), CustomMethod.create(method));
    }

    public ServletResponse service(ServletRequest request) {
        logger.debug("Start Servlet Service : {}", request.createMappingKey());
        Map<String, String> inputs = request.getParameters();
        Cookie cookie = request.getCookie();
        Model model = new Model();
        Object response = customMethod.invoke(controller, inputs, cookie, model);
        return new ServletResponse(response, model, cookie);
    }

    public void destroy() {
        logger.debug("Destroy Servlet");
    }
}
