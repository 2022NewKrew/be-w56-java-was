package framework.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Controller 클래스 및 메소드에 붙일 Annotation,
 * URI와 요청 방식을 담음
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequestMapping {
    String value() default "/";
    String requestMethod() default "";
}
