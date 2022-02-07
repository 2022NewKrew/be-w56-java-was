package servlet;

import http.Cookie;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MethodParameters {
    private final Map<ParameterConstructor, Fields> parameters;

    private MethodParameters(Map<ParameterConstructor, Fields> parameters) {
        this.parameters = parameters;
    }

    public static MethodParameters create(Method method) {
        Map<ParameterConstructor, Fields> parameters = new LinkedHashMap<>();
        for (var parameter : method.getParameterTypes()) {
            if (parameter != Cookie.class && parameter != Model.class) {
                Fields fields = Fields.create(parameter.getDeclaredFields());
                ParameterConstructor constructor = ParameterConstructor.create(parameter, fields);
                parameters.put(constructor, fields);
            }
        }
        return new MethodParameters(parameters);
    }

    public List<Object> makeParameters(Map<String, String> inputs) {
        List<Object> objects = new ArrayList<>();
        for (var parameter : parameters.entrySet()) {
            Object[] fields = parameter.getValue().makeFieldObjects(inputs);
            objects.add(parameter.getKey().makeInstance(fields));
        }
        return objects;
    }

    public boolean isEmpty() {
        return parameters.isEmpty();
    }
}
