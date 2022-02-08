package webserver.request.process;

import annotation.HttpCookie;
import webserver.http.request.HttpRequest;

import java.lang.reflect.Parameter;
import java.util.Objects;

public class CookieProcessor {

    public static Object process(Parameter parameter, HttpRequest httpRequest) {
        HttpCookie annotation = parameter.getAnnotation(HttpCookie.class);
        if(!Objects.isNull(annotation)) {
            Object result = httpRequest.getCookie(annotation.name());
            return result != null ? result : "";
        }
        return null;
    }
}
