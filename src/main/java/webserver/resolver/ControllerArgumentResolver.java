package webserver.resolver;

import http.HttpRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String SETTER_METHOD_PREFIX = "set";

    private static String getField(String methodName) {
        String field = methodName.substring(SETTER_METHOD_PREFIX.length());
        return Character.toLowerCase(field.charAt(0)) + field.substring(1);
    }

    @Override
    public boolean supportsParameter(Class<?> clazz, HttpRequest httpRequest) {
        List<String> parameters = Arrays.stream(clazz.getDeclaredFields())
            .map(Field::getName)
            .collect(Collectors.toList());
        return httpRequest.hasAllParameters(parameters);
    }

    @Override
    public Object resolveArgument(Object instance, HttpRequest httpRequest) throws Exception {
        Method[] methods = instance.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith(SETTER_METHOD_PREFIX)) {
                String field = getField(method.getName());
                method.invoke(instance, httpRequest.getParameter(field));
            }
        }
        return instance;
    }
}
