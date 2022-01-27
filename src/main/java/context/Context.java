package context;

import annotation.Controller;
import annotation.PostMapping;
import com.google.common.collect.Maps;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Context {

    private static final String CONTROLLER_BASE_PACKAGE = "controller";
    private static final Logger log = LoggerFactory.getLogger(Context.class);

    private static Set<Class<?>> controllerAClasses;
    private static Map<Class<?>, Object> controllerObjects = Maps.newHashMap();
    private static Map<String, Pair> postMappingMethods = Maps.newHashMap();

    public static void init() {
        try {
            initControllerObjects();
            initPostMappingMethods();
        } catch (Exception e) {
            log.error("Exception occurred when initializing context: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initControllerObjects() throws Exception {
        controllerAClasses = new Reflections(
            CONTROLLER_BASE_PACKAGE).getTypesAnnotatedWith(
            Controller.class);
        for (Class<?> controllerAclass : controllerAClasses) {
            controllerObjects.put(controllerAclass,
                controllerAclass.getConstructor().newInstance());
        }
    }

    private static void initPostMappingMethods() {
        for (Class<?> controller : controllerAClasses) {
            Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(
                    ClasspathHelper.forClass(controller))
                .setScanners(new MethodAnnotationsScanner()));
            Set<Method> methods = reflections.getMethodsAnnotatedWith(
                PostMapping.class);
            for (Method method : methods) {
                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                postMappingMethods.put(postMapping.value(),
                    new Pair(controllerObjects.get(controller), method));
            }
        }
    }

    public static boolean existsPostMappingUri(String uri) {
        return postMappingMethods.containsKey(uri);
    }

    public static Object invokePostMappingMethod(String uri, Object... object)
        throws Exception {
        Pair pair = postMappingMethods.get(uri);
        return pair.getMethod().invoke(pair.getObject(), object);
    }

    static class Pair {

        Object object;
        Method method;

        Pair(Object object, Method method) {
            this.object = object;
            this.method = method;
        }

        Object getObject() {
            return object;
        }

        Method getMethod() {
            return method;
        }
    }
}
