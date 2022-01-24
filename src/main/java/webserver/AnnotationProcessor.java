package webserver;

import annotation.GetMapping;
import annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnnotationProcessor {

    private AnnotationProcessor() { }

    private static final Logger log = LoggerFactory.getLogger(AnnotationProcessor.class);

    public static <T> String invokeAnnotations(Class clazz, String path, HttpMethod httpMethod) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        T instance = (T) clazz.getDeclaredConstructor().newInstance();

        Method[] methods = instance.getClass().getDeclaredMethods();
        String result = "";
        for(Method method : methods){
            GetMapping getMapping = method.getAnnotation(GetMapping.class);

            if(getMapping != null && HttpMethod.GET == httpMethod) {
                if(getMapping.path().equals(path)) {
                    log.debug("method : {}  url : {} ", httpMethod, path);
                    result = (String) method.invoke(instance);
                }
            }

            PostMapping postMapping = method.getAnnotation(PostMapping.class);
            if(postMapping != null && HttpMethod.POST == httpMethod) {
                if(postMapping.path().equals(path)) {
                    log.debug("method : {}  url : {} ", httpMethod, path);
                    result = (String) method.invoke(instance);
                }
            }
        }

        return result;
    }
}
