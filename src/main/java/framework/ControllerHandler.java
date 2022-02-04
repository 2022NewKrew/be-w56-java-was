package framework;


import com.fasterxml.jackson.databind.ObjectMapper;
import framework.annotation.*;
import framework.controller.FaviconController;
import framework.controller.css.CssController;
import framework.controller.view.ViewController;
import framework.http.request.HttpRequest;
import framework.http.response.HttpResponse;
import framework.http.response.HttpResponseHeader;
import framework.http.response.HttpStatus;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ControllerHandler {
    public static HttpResponse run(HttpRequest httpRequest) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        String path = httpRequest.getPath();
        if (path.startsWith("/css") || path.startsWith("/favicon.ico") || path.startsWith("/fonts") || path.startsWith("/js")) {
            return treatUnspecifiedRequest(httpRequest);
        }

        Set<Class<?>> typesAnnotatedWith = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("cafe")))
                .getTypesAnnotatedWith(Controller.class);

        String method = httpRequest.getMethod();

        for (Class<?> controller : typesAnnotatedWith) {
            for (Method classMethod : controller.getDeclaredMethods()) {
                if (classMethod.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping annotation = classMethod.getAnnotation(RequestMapping.class);
                    if (!isValidMethod(annotation, path, method)) {
                        continue;
                    }
                    Object[] parameterObject = getParameterObject(httpRequest, classMethod);
                    Object routerObject = controller.getConstructor().newInstance();
                    try {
                        return (HttpResponse) classMethod.invoke(routerObject, parameterObject);
                    } catch (InvocationTargetException e) {
                        return handlingException(e.getCause());
                    }
                }
            }
        }

        return new HttpResponse(HttpStatus.NOT_FOUND, new HttpResponseHeader());
    }

    private static Object[] getParameterObject(HttpRequest httpRequest, Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        ObjectMapper objectMapper = new ObjectMapper();

        List<Object> params = new ArrayList<>();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] == HttpRequest.class) {
                params.add(httpRequest);
            } else if (QueryString.class.isInstance(parameterAnnotations[i][0])) {
                QueryString queryStringAnnotation = (QueryString) parameterAnnotations[i][0];
                params.add(httpRequest.getQueryString(queryStringAnnotation.name()));
            } else if (RequestBody.class.isInstance(parameterAnnotations[i][0])) {
                params.add(objectMapper.convertValue(httpRequest.getRequestBodyMap(), parameterTypes[i]));
            }
        }

        return params.toArray();
    }

    private static boolean isValidMethod(RequestMapping requestMapping, String path, String method) {
        if (!requestMapping.method().equals(method)) {
            return false;
        }

        if (!requestMapping.value().equals(path)) {
            return false;
        }

        return true;
    }

    private static HttpResponse handlingException(Throwable exception) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        try {
            Set<Class<?>> typesAnnotatedWith = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage("cafe")))
                    .getTypesAnnotatedWith(ControllerAdvice.class);

            for (Class<?> controller : typesAnnotatedWith) {
                for (Method classMethod : controller.getDeclaredMethods()) {
                    if (classMethod.isAnnotationPresent(ExceptionHandler.class)) {
                        ExceptionHandler annotation = classMethod.getAnnotation(ExceptionHandler.class);
                        Object routerObject = controller.getConstructor().newInstance();
                        isValidMethodForExceptionHandler(annotation, exception);
                        return (HttpResponse) classMethod.invoke(routerObject, exception);
                    }
                }
            }
        } catch (Exception e) {
            handlingFrameworkException(exception);
        }

        return new HttpResponse(HttpStatus.NOT_FOUND, new HttpResponseHeader());
    }

    private static HttpResponse handlingFrameworkException(Throwable exception) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<?> aClass = Class.forName("framework.advice.FrameWorkExceptionHandler");

        for (Method classMethod : aClass.getDeclaredMethods()) {
            if (classMethod.isAnnotationPresent(ExceptionHandler.class)) {
                ExceptionHandler annotation = classMethod.getAnnotation(ExceptionHandler.class);
                Object routerObject = aClass.getConstructor().newInstance();
                isValidMethodForExceptionHandler(annotation, exception);
                return (HttpResponse) classMethod.invoke(routerObject, exception);
            }
        }

        return new HttpResponse(HttpStatus.NOT_FOUND, new HttpResponseHeader());
    }

    private static boolean isValidMethodForExceptionHandler(ExceptionHandler exceptionHandler, Throwable exception) {
        return Arrays.asList(exceptionHandler.values()).contains(exception);
    }

    private static HttpResponse treatUnspecifiedRequest(HttpRequest httpRequest) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String path = httpRequest.getPath();
        Class<?> klass = null;

        if (path.startsWith("/css")) {
            klass = CssController.class;
        } else if (path.equals("/favicon.ico")) {
            klass = FaviconController.class;
        } else {
            klass = ViewController.class;
        }

        for (Method classMethod : klass.getDeclaredMethods()) {
            if (classMethod.isAnnotationPresent(RequestMapping.class)) {
                Object routerObject = klass.getConstructor().newInstance();
                return (HttpResponse) classMethod.invoke(routerObject, httpRequest);
            }
        }

        return new HttpResponse(HttpStatus.NOT_FOUND, new HttpResponseHeader());
    }
}
