package webserver.adapter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import webserver.annotation.Controller;
import webserver.annotation.RequestMapping;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.util.ModelAndView;

public class UrlHandlerAdapter implements HandlerAdapter {
    private static final Set<Class<?>> CONTROLLER_CLASS;

    static {
        CONTROLLER_CLASS = new Reflections("").getTypesAnnotatedWith(Controller.class);
    }


    @Override
    public boolean isSupport(HttpRequest request) {
        return findMethod(request).isPresent() ? true : false;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) throws IOException {
        Method method = findMethod(request).get();

        try {
            Method getInstanceMethod = method.getDeclaringClass().getMethod("getInstance");
            Object instance = getInstanceMethod.invoke(null);

            ModelAndView modelAndView = (ModelAndView) method.invoke(
                    instance, request, response
            );

            response.send(modelAndView);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    private Optional<Method> findMethod(HttpRequest request) {
        Optional<Method> findMethod = Optional.empty();

        for (Class controllerClass : CONTROLLER_CLASS) {
            findMethod = Arrays.stream(controllerClass.getDeclaredMethods())
                               .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                               .filter(method -> isRequestMapping(method, request))
                               .findFirst();
        }

        return findMethod;
    }

    private boolean isRequestMapping(Method method, HttpRequest httpRequest) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);

        return StringUtils.equals(annotation.value(), httpRequest.getUrl())
                && (annotation.method() == httpRequest.getMethod());
    }
}
