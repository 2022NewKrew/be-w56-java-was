package annotation;

import http.HttpMethod;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@RequestMapping(method = HttpMethod.GET)
public @interface GetMapping {
     String value();
     HttpMethod method = HttpMethod.GET;
}
