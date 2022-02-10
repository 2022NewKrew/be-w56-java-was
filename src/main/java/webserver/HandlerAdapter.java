package webserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.model.ModelAndView;
import webserver.model.http.HttpRequest;
import webserver.model.http.HttpResponse;
import webserver.enums.HttpStatus;
import webserver.enums.MIME;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(HandlerAdapter.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final DIContainer diContainer = new DIContainer();
    private static final HandlerMapping handlerMapping = new HandlerMapping(diContainer.getBeans());

    public static ModelAndView handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            return httpResponse.getMime() != MIME.HTML?
                    new ModelAndView(httpRequest.getUri()) : executeController(httpRequest, httpResponse);
        } catch (Exception e) {
            log.error(e.getMessage() + " " + e.getCause());
            throw new RuntimeException();
        }
    }

    private static ModelAndView executeController(HttpRequest httpRequest, HttpResponse httpResponse) throws InvocationTargetException, IllegalAccessException {
        // find target method from http request
        HandlerMapping.Pair classAndMethod = handlerMapping.findMethod(httpRequest);
        Method targetMethod = classAndMethod.getMethod();

        // get parameters from http request
        Map<String, String> params = HttpRequestUtils.parseQueryString(httpRequest.getBody());

        Object object = classAndMethod.getObject();

        List<Object> paramObjects = new ArrayList<>();
        for (Class<?> clazz : targetMethod.getParameterTypes()) {
            if (clazz.equals(HttpResponse.class))
                paramObjects.add(httpResponse);
            else if (clazz.equals(HttpRequest.class))
                paramObjects.add(httpRequest);
            else
                paramObjects.add(mapper.convertValue(params, clazz));
        }

        if (targetMethod.getParameterCount() != 0)
            return (ModelAndView) targetMethod
                    .invoke(object, paramObjects.toArray());
        return (ModelAndView) targetMethod.invoke(object);
    }
}
