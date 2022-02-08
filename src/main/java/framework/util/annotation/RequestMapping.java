package framework.util.annotation;

import framework.http.HttpMethod;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RequestMapping {
    HttpMethod method();

    String url();
}
