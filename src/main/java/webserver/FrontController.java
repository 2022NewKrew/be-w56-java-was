package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.enums.HttpStatus;
import webserver.enums.MIME;

import java.lang.reflect.Constructor;

public class FrontController {
    private static final Logger log = LoggerFactory.getLogger(FrontController.class);

    public static HttpResponse handle(HttpRequest httpRequest) {
        MIME mime = MIME.parse(httpRequest.getUri());

        // request for the static files
        if (mime != MIME.HTML) {
            HttpResponse response = HttpResponse.httpStatus(HttpStatus.OK).setView(httpRequest.getUri());
            response.setMime(mime);
            return response;
        }

        HandlerMapping.Pair classAndMethod = HandlerMapping.findMethod(httpRequest);
        try {
            Constructor<?> constructor = classAndMethod.getClazz().getConstructor();
            Object node = constructor.newInstance();
            HttpResponse response = (HttpResponse) classAndMethod.getMethod().invoke(node);
            response.setMime(mime);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage() + " " + e.getCause());
            return HttpResponse.httpStatus(HttpStatus.NOT_FOUND).body("");
        }
    }
}
