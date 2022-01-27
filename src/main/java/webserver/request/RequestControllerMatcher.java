package webserver.request;

import controller.Controller;
import webserver.header.HttpMethod;
import webserver.response.Response;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;

public class RequestControllerMatcher {

    private final HttpMethod httpMethod;
    private final String url;
    private static final Response NOT_MATCH = null;

    public RequestControllerMatcher(HttpMethod httpMethod, String url) {
        this.httpMethod = httpMethod;
        this.url = url;
    }

    public Response match(Map<String, String> queryMap, Map<String, String> bodyMap)
            throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        Response result = RequestProcessor.process(Controller.class, url, httpMethod, queryMap, bodyMap);
        if(Objects.isNull(result)) {
            return new Response(url);
        }
        return result;
    }


}
