package webserver.processor.exception;

import http.HttpResponse;

public interface ExceptionResolver {
    boolean supports(Exception e);
    HttpResponse resolve(Exception e);
}
