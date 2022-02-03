package framework.util;

import framework.RequestMapping;
import mvc.controller.Controller;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ReflectionManager {

    public static Map<Method, Object> getMethodObjectMap(Set<Controller> controllerList) {
        Map<Method, Object> methodObjectMap = new ConcurrentHashMap<>();
        controllerList.forEach(instance -> setMap(instance, methodObjectMap));
        return methodObjectMap;
    }

    private static void setMap(Object instance, Map<Method, Object> methodObjectMap) {
        var methods = Arrays.stream(instance.getClass().getMethods())
                .filter(ReflectionManager::isMappingMethod)
                .collect(Collectors.toSet());
        methods.forEach(method -> methodObjectMap.put(method, instance));
    }

    private static boolean isMappingMethod(Method method) {
        return method.getAnnotation(RequestMapping.class) != null;
    }
}
