package webserver.http.exception;

import webserver.http.ResponseEntity;

public interface ExceptionResolver {
    boolean supports(Throwable e);
    ResponseEntity<?> resolve(Throwable e);
}
