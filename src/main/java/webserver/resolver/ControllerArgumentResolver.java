package webserver.resolver;

import com.google.common.collect.Lists;
import http.HttpRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(Class<?> clazz, HttpRequest httpRequest) {
        List<String> parameters = Arrays.stream(clazz.getDeclaredFields())
            .map(Field::getName)
            .collect(Collectors.toList());
        return httpRequest.hasAllParameters(parameters);
    }

    @Override
    public Object resolveArgument(Object instance, HttpRequest httpRequest) throws Exception {
        Class<?> clazz = instance.getClass();
        List<Class<?>> parameterTypes = Lists.newArrayList();
        List<String> parameterNames = Lists.newArrayList();
        for (Field field : clazz.getDeclaredFields()) {
            parameterTypes.add(field.getType());
            parameterNames.add(field.getName());
        }
        Constructor<?> constructor = clazz.getDeclaredConstructor(
            parameterTypes.toArray(new Class[0]));
        Object[] args = parameterNames.stream().map(httpRequest::getParameter).toArray();
        return constructor.newInstance(args);
    }
}
