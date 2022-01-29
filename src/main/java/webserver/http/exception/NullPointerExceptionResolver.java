package webserver.http.exception;

import http.HttpHeaders;
import http.StatusCode;
import webserver.http.ResponseEntity;
import webserver.http.response.View;

import java.util.List;
import java.util.Map;

public class NullPointerExceptionResolver implements ExceptionResolver {
    @Override
    public boolean supports(Throwable e) {
        return e.getClass().equals(NullPointerException.class);
    }

    @Override
    public ResponseEntity<?> resolve(Throwable e) {
        return new ResponseEntity<>(new HttpHeaders(Map.of()), List.of(), StatusCode.INTERNAL_SERVER_ERROR, View.staticFile("/500.html"));
    }
}
