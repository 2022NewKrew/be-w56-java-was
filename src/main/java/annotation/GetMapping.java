package annotation;

import http.startline.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@RequestMapping(method = HttpMethod.GET)
public @interface GetMapping {
     String value();
     HttpMethod method = HttpMethod.GET;
}
