package webserver.request;

import annotation.GetMapping;
import annotation.PostMapping;
import annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.header.HttpMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestProcessor {

    private RequestProcessor() { }

    private static final Logger log = LoggerFactory.getLogger(RequestProcessor.class);

    public static <T> String process(Class<T> clazz, String path, HttpMethod httpMethod, Map<String, String> queryMap)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {

        T instance = clazz.getDeclaredConstructor().newInstance();

        Method[] methods = instance.getClass().getDeclaredMethods();
        String result = "";
        for(Method method : methods){
            result = getMethodProcess(path, httpMethod, queryMap, instance, result, method);
            result = postMethodProcess(path, httpMethod, instance, result, method);
        }

        return result;
    }

    private static <T> String postMethodProcess(
            String path,
            HttpMethod httpMethod,
            T instance,
            String result,
            Method method)
            throws IllegalAccessException, InvocationTargetException {

        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        if(postMapping != null && HttpMethod.POST == httpMethod && postMapping.path().equals(path)) {
            log.debug("method : {}  url : {} ", httpMethod, path);

            result = (String) method.invoke(instance);
        }
        return result;
    }

    private static <T> String getMethodProcess(
            String path,
            HttpMethod httpMethod,
            Map<String, String> queryMap,
            T instance,
            String result,
            Method method)
            throws IllegalAccessException, InvocationTargetException {

        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        if(getMapping != null && HttpMethod.GET == httpMethod && getMapping.path().equals(path)) {
            log.debug("method : {}  url : {} ", httpMethod, path);

            Parameter[] parameters = method.getParameters();
            List<Object> objects = createMethodParam(queryMap, parameters);
            result = (String) method.invoke(instance, objects.toArray());
        }
        return result;
    }

    private static List<Object> createMethodParam(Map<String, String> queryMap, Parameter[] parameters) {
        List<Object> objects = new ArrayList<>();
        for (Parameter parameter : parameters) {
            RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
            String value = queryMap.get(requestParam.name());
            if (value != null) {
                objects.add(value);
            }
        }
        return objects;
    }

}
