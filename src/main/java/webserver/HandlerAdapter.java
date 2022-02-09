package webserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.HttpStatus;
import webserver.http.enums.MIME;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class HandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(HandlerAdapter.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final DIContainer diContainer = new DIContainer();
    private static final HandlerMapping handlerMapping = new HandlerMapping(diContainer.getBeans());

    public static HttpResponse handle(HttpRequest httpRequest) {
        MIME mime = MIME.parse(httpRequest.getUri());

        try {
            HttpResponse response = mime != MIME.HTML?
                    HttpResponse.httpStatus(HttpStatus.OK).setView(httpRequest.getUri()) : executeController(httpRequest);
            response.setMime(mime);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage() + " " + e.getCause());
            return HttpResponse.httpStatus(HttpStatus.NOT_FOUND).body("");
        }
    }

    private static HttpResponse executeController(HttpRequest httpRequest) throws InvocationTargetException, IllegalAccessException {
        // find target method from http request
        HandlerMapping.Pair classAndMethod = handlerMapping.findMethod(httpRequest);
        Method targetMethod = classAndMethod.getMethod();

        // get parameters from http request
        Map<String, String> params = HttpRequestUtils.parseQueryString(httpRequest.getBody());

        Object object = classAndMethod.getObject();

        if (targetMethod.getParameterCount() != 0)
            return (HttpResponse) targetMethod
                    .invoke(object, mapper.convertValue(params, targetMethod.getParameterTypes()[0]));
        return (HttpResponse) targetMethod.invoke(object);
    }
}
