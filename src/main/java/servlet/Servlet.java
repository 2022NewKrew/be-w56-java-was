package servlet;

import http.RequestMessage;
import web.controller.UserController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Servlet{
    private final Object controller;
    private final CustomMethod customMethod;

    private Servlet(Object controller, CustomMethod customMethod) {
        this.controller = controller;
        this.customMethod = customMethod;
    }

    public static Servlet create(Method method) {
        return new Servlet(new UserController() , CustomMethod.create(method));
    }

    public String service(RequestMessage request) throws InvocationTargetException, IllegalAccessException {
        // TODO GET 수정
        Map<String, String> userDto = request.getStatusLine().getRequestTarget().getParameters().getParameters();
        return (String) customMethod.invoke(controller, userDto);
    }

    public void destroy() {
        // 컨테이너에서 전부 종료
    }
}
