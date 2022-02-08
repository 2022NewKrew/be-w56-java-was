package servlet.container;

import servlet.ServletResponse;
import servlet.view.Model;

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

    public Object invoke(Object controller, Map<String, String> inputs, ServletResponse response) throws InvocationTargetException, IllegalAccessException {
        List<Object> parameters = makeParameters(inputs, response);
        if (parameters.isEmpty()) {
            return method.invoke(controller);
        }
        return method.invoke(controller, parameters.toArray());
    }

    private List<Object> makeParameters(Map<String, String> inputs, ServletResponse response) {
        List<Object> inputParameters = parameters.makeParameters(inputs);
        if (isExist(Model.class)) {
            inputParameters.add(response.getModel());
        }
        if (isExist(ServletResponse.class)) {
            inputParameters.add(response);
        }
        return inputParameters;
    }

    private boolean isExist(Class<?> classType) {
        return Arrays.stream(method.getParameterTypes())
                .filter(parameter -> parameter == classType)
                .count() == 1;
    }
}
