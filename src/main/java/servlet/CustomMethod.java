package servlet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CustomMethod {
    Method method;
    MethodParameters parameters;

    private CustomMethod(Method method, MethodParameters parameters) {
        this.method = method;
        this.parameters = parameters;
    }

    public static CustomMethod create(Method method) {
        return new CustomMethod(method, MethodParameters.create(method));
    }

    public Object invoke(Object controller, Map<String, String> inputs) {
        // TODO 예외처리
        try {
            if (parameters.isEmpty())
                return method.invoke(controller);
            return method.invoke(controller, parameters.makeParameters(inputs));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return "";
        }
    }
}
