package webserver.router;

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

@Deprecated
public class ControllerRouter {
    private static final Set<Class<?>> CONTROLLER_CLASS;

    static {
        CONTROLLER_CLASS = new Reflections("").getTypesAnnotatedWith(Controller.class);
    }

    public static boolean routing(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        Optional<Method> findMethod = Optional.empty();

        for (Class controllerClass : CONTROLLER_CLASS) {
            findMethod = Arrays.stream(controllerClass.getDeclaredMethods())
                               .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                               .filter(method -> isRequestMapping(method, httpRequest))
                               .findFirst();

            if (findMethod.isPresent()) {
                Method method = findMethod.get();

                try {
                    Method getInstanceMethod = controllerClass.getMethod("getInstance");
                    Object instance = getInstanceMethod.invoke(null);

                    ModelAndView modelAndView = (ModelAndView) method.invoke(
                            instance, httpRequest, httpResponse
                    );

                    httpResponse.send(modelAndView);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }

                return true;
            }
        }

        return false;
    }

    private static boolean isRequestMapping(Method method, HttpRequest httpRequest) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);

        return StringUtils.equals(annotation.value(), httpRequest.getUrl())
                && (annotation.method() == httpRequest.getMethod());
    }
}
