package webserver.handler.handlerMapping;

import static webserver.IocContainer.getAllAnnotatedClass;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.annotation.Controller;
import app.http.HttpMethod;
import app.http.HttpRequest;
import webserver.handler.HandlerMethod;
import webserver.handler.mappingValidator.DeleteMappingValidator;
import webserver.handler.mappingValidator.GetMappingValidator;
import webserver.handler.mappingValidator.MappingValidator;
import webserver.handler.mappingValidator.PostMappingValidator;
import webserver.handler.mappingValidator.PutMappingValidator;

public class DefaultMapping implements HandlerMapping {
    private static final Logger log = LoggerFactory.getLogger(DefaultMapping.class);
    private static final Map<String, MappingValidator> mappingValidators = new HashMap<>();
    static {
        mappingValidators.put(HttpMethod.GET.method(), GetMappingValidator.getInstance());
        mappingValidators.put(HttpMethod.POST.method(), PostMappingValidator.getInstance());
        mappingValidators.put(HttpMethod.PUT.method(), PutMappingValidator.getInstance());
        mappingValidators.put(HttpMethod.DELETE.method(), DeleteMappingValidator.getInstance());
    }

    @Override
    public HandlerMethod getHandlerMethod(HttpRequest request) {
        Set<Class<?>> handlers = getAllAnnotatedClass(Controller.class);
        for(Class clazz : handlers) {
            Method method = getAnnotatedMethodFromClass(request, clazz);
            if(method != null) {
                return HandlerMethod.of(clazz, method);
            }
        }
        return null;
    }

    private Method getAnnotatedMethodFromClass(HttpRequest request, Class clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods) {
            if(method.isAnnotationPresent(request.getMethod().getAnnotationClass()) && isUrlValid(request, method)) {
                return method;
            }
        }
        return null;
    }

    private boolean isUrlValid(HttpRequest request, Method method) {
        MappingValidator mappingValidator = mappingValidators.get(request.getMethod().method());
        if(mappingValidator == null) {
            return false;
        }
        return mappingValidator.validateURL(request, method);
    }
}
