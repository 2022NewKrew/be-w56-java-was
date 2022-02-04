package webserver;

import exception.CustomException;
import http.HttpStatus;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerExceptionResolver {

    private static final Logger log = LoggerFactory.getLogger(HandlerExceptionResolver.class);

    public static HandlerExceptionResolver instance;

    private HandlerExceptionResolver() {
    }

    public static HandlerExceptionResolver getInstance() {
        if (instance == null) {
            instance = new HandlerExceptionResolver();
        }
        return instance;
    }

    public HttpResponse resolveException(CustomException e) {
        try {
            return HttpResponse.error(e.getStatus(), e.getMessage());
        } catch (Exception handlerEx) {
            log.error("Failure while trying to resolve exception [" + e.getClass().getName() + "]", handlerEx);
        }
        return null;
    }

    public HttpResponse resolveException(Throwable throwable) {
        if (throwable instanceof CustomException) {
            return resolveException((CustomException) throwable);
        }
        CustomException e = new CustomException(throwable.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return resolveException(e);
    }
}
