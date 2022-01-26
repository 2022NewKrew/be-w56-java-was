package webserver.dispatcher.dynamic.bind.annotation;

import webserver.request.HttpRequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = HttpRequestMethod.POST)
public @interface PostMapping {

    String[] value() default {};
}
