package webserver.request;

import controller.Controller;
import webserver.header.HttpMethod;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class RequestControllerMatcher {

    private final HttpMethod httpMethod;
    private final String url;
    private static final String NOT_MATCH = "";

    public RequestControllerMatcher(HttpMethod httpMethod, String url) {
        this.httpMethod = httpMethod;
        this.url = url;
    }

    public String match(Map<String, String> queryMap, Map<String, String> bodyMap)
            throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        String result = RequestProcessor.process(Controller.class, url, httpMethod, queryMap, bodyMap);
        if(result.equals(NOT_MATCH)) {
            return url;
        }
        return result;
    }


}
