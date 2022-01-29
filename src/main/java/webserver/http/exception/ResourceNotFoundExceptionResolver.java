package webserver.http.exception;

import http.HttpHeaders;
import http.StatusCode;
import webserver.exception.ResourceNotFoundException;
import webserver.http.ResponseEntity;
import webserver.http.response.View;

import java.util.List;
import java.util.Map;

public class ResourceNotFoundExceptionResolver implements ExceptionResolver {

    @Override
    public boolean supports(Throwable e) {
        return e.getClass().isAssignableFrom(ResourceNotFoundException.class);
    }

    @Override
    public ResponseEntity<?> resolve(Throwable e) {
        return new ResponseEntity<>(new HttpHeaders(Map.of()), List.of(), StatusCode.NOT_FOUND, View.staticFile("/404.html"));
    }
}
