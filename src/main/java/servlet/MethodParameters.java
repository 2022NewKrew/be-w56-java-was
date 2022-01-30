package servlet;

import java.lang.reflect.Method;
import java.util.*;

public class MethodParameters {
    private final Map<ParameterConstructor, Fields> parameters;

    private MethodParameters(Map<ParameterConstructor, Fields> parameters) {
        this.parameters = parameters;
    }

    public static MethodParameters create(Method method) {
        Map<ParameterConstructor, Fields> parameters = new LinkedHashMap<>();
        for (var parameter : method.getParameterTypes()) {
            Fields fields = Fields.create(parameter.getDeclaredFields());
            ParameterConstructor constructor = ParameterConstructor.create(parameter, fields);
            parameters.put(constructor, fields);
        }
        return new MethodParameters(parameters);
    }

    public Object[] makeParameters(Map<String, String> inputs) {
        List<Object> objects = new ArrayList<>();
        for (var parameter : parameters.entrySet()) {
            Object[] fields = parameter.getValue().makeFieldObjects(inputs);
            objects.add(parameter.getKey().makeInstance(fields));
        }
        return objects.toArray();
    }

    public boolean isEmpty() {
        return parameters.isEmpty();
    }
}
