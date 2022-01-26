package webserver.annotation;

import webserver.domain.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequestMapping {
    RequestMethod method() default RequestMethod.GET;

    String value() default "";
}
