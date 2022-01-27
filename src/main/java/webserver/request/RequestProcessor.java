package webserver.request;

import annotation.GetMapping;
import annotation.PostMapping;
import annotation.RequestBody;
import annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.header.HttpMethod;
import webserver.response.Response;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestProcessor {

    private RequestProcessor() { }

    private static final Logger log = LoggerFactory.getLogger(RequestProcessor.class);

    public static <T> Response process(
            Class<T> clazz,
            String path,
            HttpMethod httpMethod,
            Map<String, String> queryMap,
            Map<String, String> bodyMap)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {

        T instance = clazz.getDeclaredConstructor().newInstance();

        Method[] methods = instance.getClass().getDeclaredMethods();
        Response result = null;
        for(Method method : methods){
            result = getMethodProcess(path, httpMethod, queryMap, instance, result, method);
            result = postMethodProcess(path, httpMethod, bodyMap, instance, result, method);
        }

        return result;
    }

    private static <T> Response postMethodProcess(
            String path,
            HttpMethod httpMethod,
            Map<String, String> bodyMap,
            T instance,
            Response result,
            Method method)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {

        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        if(postMapping != null && httpMethod.isSame(HttpMethod.POST) && postMapping.path().equals(path)) {
            log.debug("method : {}  url : {} ", httpMethod, path);

            Parameter[] parameters = method.getParameters();
            Object requestBody = createRequestBody(bodyMap, parameters);
            result = (Response) method.invoke(instance, requestBody);
        }
        return result;
    }

    private static Object createRequestBody(Map<String, String> bodyMap, Parameter[] parameters)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Object result = null;
        for (Parameter parameter : parameters) {
            if(parameter.getAnnotation(RequestBody.class) != null) {
                RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
                List<Object> objects = new ArrayList<>();
                for(Constructor constructor : parameter.getType().getDeclaredConstructors()) {
                    log.debug("Constructor : {}", constructor);
                    if (constructor.getParameters().length == bodyMap.values().size()) {
                        for (String name : requestBody.names()) {
                            objects.add(bodyMap.get(name));
                        }
                        result = constructor.newInstance(objects.toArray());
                    }
                }
            }
        }
        return result;
    }

    private static <T> Response getMethodProcess(
            String path,
            HttpMethod httpMethod,
            Map<String, String> queryMap,
            T instance,
            Response result,
            Method method)
            throws IllegalAccessException, InvocationTargetException {

        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        if(getMapping != null && httpMethod.isSame(HttpMethod.GET) && getMapping.path().equals(path)) {
            log.debug("method : {}  url : {} ", httpMethod, path);

            Parameter[] parameters = method.getParameters();
            List<Object> objects = createMethodParam(queryMap, parameters);
            result = (Response) method.invoke(instance, objects.toArray());
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
