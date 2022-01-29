package annotation;

import http.HttpMethod;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
public @interface RequestMapping {
    HttpMethod method() default HttpMethod.NONE;
    String value() default "/";
}
