package webserver.bind.annotation;

import webserver.http.request.Method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = Method.POST)
public @interface PostMapping {

    String[] value() default {};
}
