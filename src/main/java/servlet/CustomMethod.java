package servlet;

import http.Cookie;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    public Object invoke(Object controller, Map<String, String> inputs, Cookie cookie, Model model) {
        boolean isExistCookie = Arrays.stream(method.getParameterTypes()).filter(parameter -> parameter == Cookie.class).count() == 1;
        boolean isExistModel = Arrays.stream(method.getParameterTypes()).filter(parameter -> parameter == Model.class).count() == 1;
        // TODO 예외처리
        try {
            if (parameters.isEmpty() && !isExistCookie && !isExistModel) {
                return method.invoke(controller);
            }

            List<Object> inputParameters = parameters.makeParameters(inputs);
            if (isExistCookie) {
                inputParameters.add(cookie);
            }
            if (isExistModel) {
                inputParameters.add(model);
            }
            return method.invoke(controller, inputParameters.toArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return "";
        }
    }
}
