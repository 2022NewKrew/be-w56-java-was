package webserver.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class AnnotationUtils {
    public static boolean isMethodHasAnnotation(Method target, Class<? extends Annotation> annotation) {
        if (target.isAnnotationPresent(annotation)) return true;
        Annotation[] annotations = target.getDeclaredAnnotations();
        for (Annotation a : annotations) {
            if (isAnnotationHasAnnotation(a.annotationType(), annotation)) return true;
        }
        return false;
    }

    public static boolean isAnnotationHasAnnotation(Class<? extends Annotation> target, Class<? extends Annotation> annotation) {
        if (target.isAnnotationPresent(annotation)) return true;
        Annotation[] annotations = target.getDeclaredAnnotations();
        for (Annotation a : annotations) {
            if (isAnnotationHasAnnotation(a.annotationType(), annotation)) return true;
        }
        return false;
    }

    public static String getRequestMethodOfController(Method method) {
        return Arrays.stream(method.getAnnotations()).findAny().get()
                .annotationType().getAnnotation(RequestMapping.class).method().toString();
    }

    public static String getRequestURLOfController(Method method) {
        String requestMethod = getRequestMethodOfController(method);
        if (requestMethod.equals("GET")) return method.getAnnotation(GetMapping.class).value();
        if (requestMethod.equals("POST")) return method.getAnnotation(PostMapping.class).value();
        if (requestMethod.equals("PUT")) return method.getAnnotation(PutMapping.class).value();
        if (requestMethod.equals("DELETE")) return method.getAnnotation(DeleteMapping.class).value();
        return null;
    }
}
