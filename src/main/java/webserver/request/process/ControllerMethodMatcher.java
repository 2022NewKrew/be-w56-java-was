package webserver.request.process;

import webserver.http.HttpStatus;
import webserver.http.RequestLine;
import webserver.http.Response;
import webserver.http.request.HttpRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class ControllerMethodMatcher {

    public static <T> Response match (Class<T> clazz, HttpRequest httpRequest)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException{

        String path = httpRequest.getHeaderAttribute(RequestLine.PATH.name());

        T instance = clazz.getDeclaredConstructor().newInstance();
        Method[] methods = instance.getClass().getMethods();

        Response result = null;
        for(Method method : methods){
            result = MethodProcessor.process(path, httpRequest, instance, result, method);
        }

        if(Objects.isNull(result)) {
            return new Response(path, HttpStatus.OK);
        }
        return result;
    }
}
