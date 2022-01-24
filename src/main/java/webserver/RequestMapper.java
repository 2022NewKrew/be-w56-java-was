package webserver;

import controller.Controller;

import java.lang.reflect.InvocationTargetException;

public class RequestMapper {

    private final HttpMethod httpMethod;
    private final String url;

    public RequestMapper(HttpMethod httpMethod, String url) {
        this.httpMethod = httpMethod;
        this.url = url;
    }

    public String match() throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        String result = AnnotationProcessor.invokeAnnotations(Controller.class, url, httpMethod);
        if(result.equals("")) {
            return url;
        }
        return result;
    }
}
