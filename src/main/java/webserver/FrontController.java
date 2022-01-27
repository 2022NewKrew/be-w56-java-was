package webserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.enums.HttpStatus;
import webserver.enums.MIME;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

public class FrontController {
    private static final Logger log = LoggerFactory.getLogger(FrontController.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static HttpResponse handle(HttpRequest httpRequest) {
        MIME mime = MIME.parse(httpRequest.getUri());

        // request for the static files
        if (mime != MIME.HTML) {
            HttpResponse response = HttpResponse.httpStatus(HttpStatus.OK).setView(httpRequest.getUri());
            response.setMime(mime);
            return response;
        }

        // find target method
        HandlerMapping.Pair classAndMethod = HandlerMapping.findMethod(httpRequest);
        try {
            Constructor<?> constructor = classAndMethod.getClazz().getConstructor();
            Object node = constructor.newInstance();

            Map<String, String> params = HttpRequestUtils.parseQueryString(httpRequest.getBody());

            Method targetMethod = classAndMethod.getMethod();
            HttpResponse response;
            if (targetMethod.getParameterCount() != 0)
                response = (HttpResponse) targetMethod
                        .invoke(node, mapper.convertValue(params, targetMethod.getParameterTypes()[0]));
            else
                response = (HttpResponse) targetMethod.invoke(node);
            response.setMime(mime);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage() + " " + e.getCause());
            return HttpResponse.httpStatus(HttpStatus.NOT_FOUND).body("");
        }
    }
}
