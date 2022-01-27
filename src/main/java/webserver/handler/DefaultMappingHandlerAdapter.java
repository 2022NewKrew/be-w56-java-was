package webserver.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.http.HttpRequest;
import webserver.handler.typeResolver.MapTypeResolver;
import webserver.handler.typeResolver.TypeResolver;

public class DefaultMappingHandlerAdapter implements HandlerAdapter {
    private static final Map<Class<?>, TypeResolver> typeMapper = new HashMap<>();

    static {
        typeMapper.put(Map.class, MapTypeResolver.getInstance());
    }

    private static final Logger log = LoggerFactory.getLogger(DefaultMappingHandlerAdapter.class);

    @Override
    public boolean supports(Object handlerMethod) {
        if (handlerMethod != null) {
            return handlerMethod instanceof HandlerMethod;
        }
        return false;
    }

    @Override
    public String handle(HttpRequest request, HandlerMethod handlerMethod) {
        try {
            Method method = handlerMethod.getMethod();
            List<Object> arguments = getArgument(request, handlerMethod);
            if (arguments == null) {
                return (String) method.invoke(handlerMethod.getClazz().getConstructor().newInstance());
            }
            return (String) method.invoke(handlerMethod.getClazz().getConstructor().newInstance(), arguments.toArray());
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException | NoSuchMethodException | InstantiationException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public List<Object> getArgument(HttpRequest request, HandlerMethod handlerMethod) {
        List<Object> arguments = new ArrayList<>();
        Parameter[] parameters = handlerMethod.getMethod().getParameters();
        for (Parameter parameter : parameters) {
            if (typeMapper.containsKey(parameter.getType())) {
                arguments.add(typeMapper.get(parameter.getType())
                                        .getType(request));
            }
        }
        if (arguments.size() == 0) {
            return null;
        }
        return arguments;
    }
}
