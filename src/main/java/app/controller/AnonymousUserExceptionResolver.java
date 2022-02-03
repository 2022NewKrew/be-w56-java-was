package app.controller;

import http.HttpHeaders;
import http.StatusCode;
import webserver.http.ResponseEntity;
import webserver.http.exception.ExceptionResolver;
import webserver.http.response.View;

import java.util.List;
import java.util.Map;

public class AnonymousUserExceptionResolver implements ExceptionResolver {
    @Override
    public boolean supports(Throwable e) {
        return e.getClass().equals(AnonymousUserException.class);
    }

    @Override
    public ResponseEntity<?> resolve(Throwable e) {
        return new ResponseEntity<>(new HttpHeaders(Map.of()), List.of(), StatusCode.OK, View.staticFile("/user/login.html"));
    }
}
