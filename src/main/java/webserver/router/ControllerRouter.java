package webserver.router;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import app.controller.UserController;
import webserver.annotation.RequestMapping;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.util.ClassAndInstance;

public class ControllerRouter {
    private static final List<ClassAndInstance> CONTROLLER_CLASS;

    static {
        CONTROLLER_CLASS = Arrays.asList(new ClassAndInstance(UserController.class, UserController.getInstance()));
    }

    public static boolean routing(HttpRequest httpRequest, HttpResponse httpResponse) {
        Optional<Method> findMethod = Optional.empty();

        for (ClassAndInstance classAndInstance : CONTROLLER_CLASS) {
            Class aClass = classAndInstance.getaClass();

            findMethod = Arrays.stream(aClass.getDeclaredMethods())
                               .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                               .filter(method -> isRequestMapping(method, httpRequest))
                               .findFirst();

            if (findMethod.isPresent()) {
                Method method = findMethod.get();
                try {
                    Object invoke = method.invoke(classAndInstance.getInstance(), httpRequest, httpResponse);

                } catch (IllegalAccessException | InvocationTargetException e) {
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
