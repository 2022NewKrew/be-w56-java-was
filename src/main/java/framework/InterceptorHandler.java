package framework;

import framework.annotation.Interceptor;
import framework.annotation.PreHandle;
import framework.http.request.HttpRequest;
import framework.http.response.HttpResponse;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class InterceptorHandler {
    private static final Logger log = LoggerFactory.getLogger(InterceptorHandler.class);

    public static boolean preHandlerExecute(HttpRequest httpRequest, HttpResponse httpResponse) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String path = httpRequest.getPath();

        if (path.startsWith("/css") || path.startsWith("/favicon.ico") || path.startsWith("/fonts") || path.startsWith("/js")) {
            return true;
        }

        Set<Class<?>> typesAnnotatedWith = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("cafe")))
                .getTypesAnnotatedWith(Interceptor.class);

        String method = httpRequest.getMethod();

        for (Class<?> interceptor : typesAnnotatedWith) {
            for (Method classMethod : interceptor.getDeclaredMethods()) {
                if (classMethod.isAnnotationPresent(PreHandle.class)) {
                    PreHandle annotation = classMethod.getAnnotation(PreHandle.class);
                    if (isExcludePath(annotation, path)) {
                        continue;
                    }

                    if (!isIncludePath(annotation, path)) {
                        continue;
                    }
                    Object routerObject = interceptor.getConstructor().newInstance();
                    try {
                        boolean resultOfInterceptor = (boolean) classMethod.invoke(routerObject, httpRequest, httpResponse);
                        if (!resultOfInterceptor) {
                            return false;
                        }
                    } catch (InvocationTargetException e) {
                        log.error(e.getMessage());
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private static boolean isIncludePath(PreHandle preHandleAnnotation, String path) {
        return Arrays.stream(preHandleAnnotation.includePathPattern()).anyMatch(includePathPattern -> path.matches(includePathPattern));
    }

    private static boolean isExcludePath(PreHandle preHandleAnnotation, String path) {
        return Arrays.stream(preHandleAnnotation.excludePathPattern()).anyMatch(excludePathPattern -> path.matches(excludePathPattern));
    }
}
